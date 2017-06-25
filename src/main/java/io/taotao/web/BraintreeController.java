package io.taotao.web;

import com.braintreegateway.*;
import io.taotao.bean.PaymentError;
import io.taotao.bean.Record;
import io.taotao.dao.RecordRepository;
import io.taotao.util.BraintreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller
public class BraintreeController {
    private BraintreeGateway gateway = BraintreeUtil.gateway;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<Object, Object> operations;

    @RequestMapping(value = "/braintree", method = RequestMethod.GET)
    public String root(Model model) {
        String clientToken = gateway.clientToken().generate();
        model.addAttribute("clientToken", clientToken);

        return "braintree/index";
    }

    @RequestMapping(value = "/braintree", method = RequestMethod.POST)
    public String pay(@RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName,
                      @RequestParam("phone") String phone, @RequestParam("amount") String amount,
                      @RequestParam("currency") String currency, @RequestParam("payment_method_nonce") String nonce,
                      Model model) {

        BigDecimal decimalAmount = new BigDecimal(amount);
        String accountId = BraintreeUtil.getAccountId(currency);

        TransactionRequest request = new TransactionRequest()
                .amount(decimalAmount)
                .paymentMethodNonce(nonce).merchantAccountId(accountId)
                .options().submitForSettlement(true).done();

        Result<Transaction> result = gateway.transaction().sale(request);

        Record record = new Record();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setPhone(phone);
        record.setAmount(Double.parseDouble(amount));
        record.setCurrency(currency);
        record.setPaymentType(2);

        PaymentError error = null;
        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            System.out.println("Success!: " + transaction.getId());
            record.setPaymentId(transaction.getId());
            record.setCreateTime(transaction.getCreatedAt().getTime());
            operations.set(transaction.getId(), record);
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            String message = "Error processing transaction: " + "  Status: " + transaction.getStatus()
                    + "  Code: " + transaction.getProcessorResponseCode()
                    + "  Text: " + transaction.getProcessorResponseText();
            record.setError(message) ;
            error = new PaymentError();
            error.setMessage(message);
        } else {
            String message = "";
            for (ValidationError e : result.getErrors().getAllDeepValidationErrors()) {
                System.out.println("Attribute: " + e.getAttribute());
                System.out.println("  Code: " + e.getCode());
                System.out.println("  Message: " + e.getMessage());

                message += "Attribute: " + e.getAttribute();
                message += "  Code: " + e.getCode();
                message += "  Message: " + e.getMessage();
            }
            record.setError(message);
            error = new PaymentError();
            error.setMessage(message);
        }

        record.setStatus(result.isSuccess());
        recordRepository.save(record);

        model.addAttribute("isSuccess", result.isSuccess());
        model.addAttribute("error", error);
        model.addAttribute("record", record);

        return "braintree/result";
    }
}
