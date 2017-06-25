package io.taotao.web;

import com.google.gson.Gson;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import io.taotao.bean.PaymentError;
import io.taotao.bean.Record;
import io.taotao.dao.RecordRepository;
import io.taotao.util.CardUtil;
import io.taotao.util.PaypalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PaypalController {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<Object, Object> operations;

    @RequestMapping(value = "/paypal", method = RequestMethod.GET)
    public String root(Model model) {
        return "paypal/index";
    }

    @RequestMapping(value = "/paypal", method = RequestMethod.POST)
    public String pay(@RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName,
                      @RequestParam("amount") String price, @RequestParam("currency") String currency,
                      @RequestParam("card-number") String number, @RequestParam("card-expiration") String expiration,
                      @RequestParam("card-cvv") String ccv, @RequestParam("phone") String phone, Model model) {
        number = number.replaceAll("\\s", "");
        String [] exp = expiration.split("/");

        CreditCard creditCard = new CreditCard();
        creditCard.setCvv2(ccv);
        creditCard.setExpireMonth(Integer.parseInt(exp[0]));
        creditCard.setExpireYear(Integer.parseInt(exp[1]));
        creditCard.setFirstName(firstName);
        creditCard.setLastName(lastName);
        creditCard.setNumber(number);

        String type = CardUtil.getCreditCardType(number);
        creditCard.setType(type);

        // ###Amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(price);

        // ###Record
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("sandbox sample payment");

        // The Payment creation API requires a list of
        // Record; add the created `Record`
        // to a List
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // ###FundingInstrument
        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCard);

        // The Payment creation API requires a list of
        // FundingInstrument; add the created `FundingInstrument`
        // to a List
        List<FundingInstrument> fundingInstrumentList = new ArrayList<>();
        fundingInstrumentList.add(fundingInstrument);

        // ###Payer
        Payer payer = new Payer();
        payer.setFundingInstruments(fundingInstrumentList);
        payer.setPaymentMethod("credit_card");

        // ###Payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        Record record = new Record();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setPhone(phone);
        record.setAmount(Double.parseDouble(price));
        record.setCurrency(currency);
        record.setPaymentType(1);

        Payment createdPayment = null;
        PaymentError error = null;
        boolean isSuccess = true;

        try {
            APIContext context = PaypalUtil.getPaypalContext(currency);
            createdPayment = payment.create(context);
            record.setPaymentId(payment.getId());
            record.setPaymentId(createdPayment.getId());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(createdPayment.getCreateTime()
                    .replace("T", " ")
                    .replace("Z", ""));
            record.setCreateTime(date);
        } catch (PayPalRESTException e) {
            String message = e.getMessage();
            int index = message.indexOf("{\"");
            if (index > -1) {
                Gson gson = new Gson();
                error = gson.fromJson(message.substring(index), PaymentError.class);
            } else {
                error = new PaymentError();
                error.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            error = new PaymentError();
            error.setMessage(e.getMessage());
        }

        if (error != null) {
            isSuccess = false;
            record.setError(error.getMessage());
        }

        record.setStatus(isSuccess);
        recordRepository.save(record);
        if (createdPayment != null) {
            operations.set(createdPayment.getId(), record);
        }

        model.addAttribute("isSuccess", isSuccess);
        model.addAttribute("error", error);
        model.addAttribute("record", record);

        return "paypal/result";
    }
}
