
$(function () {
    $('#amount').on("input propertychange", function() {
        var val = $(this).val();
        var reg = /^\d+(\.\d{1,2}[0]*)?$/;
        var valid = reg.test(val);

        var parent = $('#amount').parent();
        if (!valid) {
            parent.addClass('has-error');
        } else {
            parent.removeClass('has-error');
        }
    });

    $('.dropdown-currency li').click(function () {
        var text = $(this).text();

        $('#currency').text(text);
        $('#currency-h').val(text);
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

        $('#my-checkout-form').submit();
    });

    
});

