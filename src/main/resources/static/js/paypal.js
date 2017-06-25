
$(function () {
    $('#card-number').on("input propertychange", function() {
        var val = $(this).val();
        val = val.replace(/(\d{4})(?=\d)/g, "$1 ");
        if (val.length > 19) {
            val = val.substring(0, 19);
        }
        $(this).val(val);

        var v = val.replace(/\s/g, '');
        var type  = validate(v);

        var el = $('#card-number');
        if (v.length < 16) {
            el.parent()
                .removeClass(' has-success')
                .removeClass('has-error');
        } else {
            if (!type) {
                el.parent()
                    .removeClass(' has-success')
                    .addClass('has-error');
            } else {
                el.parent()
                    .removeClass(' has-error')
                    .addClass(' has-success');
            }
        }

        var currency = $('#currency-h').val();
        if (currency !== 'USD' && type === 'amex') {
            el.next().removeClass('hidden');
        } else {
            el.next().addClass('hidden')
                .text('The AMEX card is possible to use only for USD.');
        }

    }).blur(function () {
        var val = $(this).val();
        val = val.replace(/\s/g, '');

        if (val.length < 16) {
            var valid  = validate(val);

            if (!valid) {
                $('#card-number').parent()
                    .removeClass(' has-success')
                    .addClass('has-error');
            } else {
                $('#card-number').parent()
                    .removeClass(' has-error')
                    .addClass(' has-success');
            }
        }
    });

    $('#card-cvv').on("input propertychange", function() {
        var val = $(this).val();
        if (val.length > 3) {
            val = val.substring(0, 3);
        }
        $(this).val(val);

        var reg = /\d{3}/;
        var valid = reg.test(val);

        var el = $('#card-cvv').parent();
        if (!valid) {
            el.addClass('has-error').removeClass(' has-success');
        } else {
            el.removeClass('has-error').addClass(' has-success');
        }
    });

    $('#card-expiration').on("input propertychange", function() {
        var val = $(this).val();
        if (val.length > 7) {
            val = val.substring(0, 7);
        }
        $(this).val(val);

        var reg = /(0?[1-9]|1[0-2])\/2[0-9]{3}/;
        var valid = reg.test(val);

        var el = $('#card-expiration').parent();
        if (!valid) {
            el.addClass('has-error').removeClass(' has-success');
        } else {
            el.removeClass('has-error').addClass(' has-success');
        }
    });

    $('#amount').on("input propertychange", function() {
        var val = $(this).val();
        var reg = /^\d+(\.\d{1,2}[0]*)?$/;
        var valid = reg.test(val);

        var el = $('#amount').parent();
        if (!valid) {
            el.addClass('has-error').removeClass(' has-success');
        } else {
            el.removeClass('has-error').addClass(' has-success');
        }
    });

    function validate(number) {
        var regVisa = /^4[0-9]{12}(?:[0-9]{3})?$/;
        var regMaster = /^5[1-5][0-9]{14}$/;
        var regExpress = /^3[47][0-9]{13}$/;
        var regDiscover = /^6(?:011|5[0-9]{2})[0-9]{12}$/;
        var regJCB = /^(?:2131|1800|35\d{3})\d{11}$/;

        if (regVisa.test(number)) {
            return 'visa';
        }

        if (regMaster.test(number)) {
            return 'master';
        }

        if (regExpress.test(number)) {
            return 'amex';
        }

        if (regDiscover.test(number)) {
            return 'discover';
        }

        if (regJCB.test(number)) {
            return 'jcb';
        }

        return null;
    }
    
    $('.dropdown-currency li').click(function () {
        var text = $(this).text();
        var el = $('#card-number');
        var number = el.val().replace(/\s/g, '');
        $('#currency').text(text);
        $('#currency-h').val(text);

        if (text !== 'USD' && validate(number) === 'amex') {
            el.next().removeClass('hidden');
        } else {
            el.next().addClass('hidden')
                .text('The AMEX card is possible to use only for USD.');
        }
    });

    $('.btn-submit').click(function () {
        var firstName = $('#first-name');
        if (firstName.val().trim().length === 0) {
            firstName.parent().addClass('has-error');
            firstName.next().removeClass('hidden');
            return;
        } else {
            firstName.parent().removeClass('has-error');
            firstName.next().addClass('hidden');
        }

        var lastName = $('#last-name');
        if (lastName.val().trim().length === 0) {
            lastName.parent().addClass('has-error');
            lastName.next().removeClass('hidden');
            return;
        } else {
            lastName.parent().removeClass('has-error');
            lastName.next().addClass('hidden');
        }

        var phone = $('#phone');
        if (phone.val().trim().length === 0) {
            phone.parent().addClass('has-error');
            phone.next().removeClass('hidden');
            return;
        } else {
            phone.parent().removeClass('has-error');
            phone.next().addClass('hidden');
        }

        var amount = $('#amount');
        if (amount.val().trim().length === 0) {
            amount.closest('.form-group').addClass('has-error');
            amount.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The amount should not be empty.');
            return;
        } else {
            amount.closest('.form-group')
                .removeClass('has-error');
            amount.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        var reg = /^\d+(\.\d{1,2}[0]*)?$/;
        var valid = reg.test(amount.val());

        if (!valid) {
            amount.closest('.form-group').addClass('has-error');
            amount.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The amount is not valid.');
            return;
        } else {
            amount.closest('.form-group')
                .removeClass('has-error');
            amount.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        var number = $('#card-number');
        if (number.val().trim().length === 0) {
            number.parent().addClass('has-error');
            number.next().removeClass('hidden')
                .text('The card number should not be empty.');
            return;
        } else {
            number.parent().removeClass('has-error');
            number.next().addClass('hidden');
        }

        var type = validate(number.val().replace(/\s/g, ''));
        if (type === null) {
            number.parent().addClass('has-error');
            number.next().removeClass('hidden')
                .text('The card number is not valid.');
            return;
        } else {
            number.parent().removeClass('has-error');
            number.next().addClass('hidden');
        }

        var currency = $('#currency-h').val();
        if (type === 'amex' && currency !== 'USD') {
            number.parent().addClass('has-error');
            number.next().removeClass('hidden')
                .text('The AMEX card is possible to use only for USD.');
            return;
        } else {
            number.parent().removeClass('has-error');
            number.next().addClass('hidden');
        }

        var cvv = $('#card-cvv');
        if (cvv.val().trim().length === 0) {
            cvv.closest('.form-group').addClass('has-error');
            cvv.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The cvv should not be empty.');
            return;
        } else {
            cvv.closest('.form-group')
                .removeClass('has-error');
            cvv.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        var cvvReg = /\d{3}/;
        var cvvValid = cvvReg.test(cvv.val());

        if (!cvvValid) {
            cvv.closest('.form-group').addClass('has-error');
            cvv.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The cvv is not valid.');
            return;
        } else {
            cvv.closest('.form-group')
                .removeClass('has-error');
            cvv.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        var expiration = $('#card-expiration');
        if (expiration.val().trim().length === 0) {
            expiration.closest('.form-group').addClass('has-error');
            expiration.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The card expiration should not be empty.');
            return;
        } else {
            expiration.closest('.form-group')
                .removeClass('has-error');
            expiration.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        var expReg = /(0?[1-9]|1[0-2])\/2[0-9]{3}/;
        var expValid = expReg.test(expiration.val());
        if (!expValid) {
            expiration.closest('.form-group').addClass('has-error');
            expiration.closest('.form-group')
                .find('.form-group-error')
                .removeClass('hidden')
                .text('The card expiration is not valid.');
            return;
        } else {
            expiration.closest('.form-group')
                .removeClass('has-error');
            expiration.closest('.form-group')
                .find('.form-group-error')
                .addClass('hidden');
        }

        $('#my-checkout-form').submit(); 
    });

});

