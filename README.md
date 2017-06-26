### Configuration

* Paypal and Braintree merchant account setting in ```config.properties```.

* Database and Redis setting in ```resources/application.properties```.

* Database script ```db.sql```.

### Run

* running the Application

```
gradle build
java -jar build/libs/payment-app-0.1.0.jar
```

* Paypal payment: http://localhost:8080/paypal

* Braintree payment: http://localhost:8080/braintree

* Check payment: http://localhost:8080/check


### Test Cards:

* Paypal

Master: 5136 3333 3333 3335

USD/EUR/AUD/HKD

* Braintree:

Visa: 4111 1111 1111 1111

USD/HKD/JPY

More test card numbers: https://docs.adyen.com/developers/payments/test-cards/test-card-numbers
