package io.taotao.web;

import io.taotao.bean.Record;
import io.taotao.dao.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class CheckController {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<Object, Object> operations;

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String show(){
        return "check/index";
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public String list(@RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName,
                       @RequestParam("payment-id") String paymentId, Model model){

        // Find from Redis first
        Record r = (Record) operations.get(paymentId);
        if (r != null && firstName.equals(r.getFirstName())
                && lastName.equals(r.getLastName())) {
            model.addAttribute("record", r);
            model.addAttribute("count", 1);

            return "check/record";
        }

        // If not found find it from database
        Record record = recordRepository.findByFirstNameAndLastNameAndPaymentId(firstName, lastName, paymentId);
        int count = 0;
        if (record != null) {
            count = 1;
            operations.set(paymentId, record);
        }

        model.addAttribute("record", record);
        model.addAttribute("count", count);

        return "check/record";
    }
}
