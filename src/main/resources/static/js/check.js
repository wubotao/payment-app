
$(function () {
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

        var paymentId = $('#payment-id');
        if (paymentId.val().trim().length === 0) {
            paymentId.parent().addClass('has-error');
            paymentId.next().removeClass('hidden');
            return;
        } else {
            paymentId.parent().removeClass('has-error');
            paymentId.next().addClass('hidden');
        }

        $('#my-checkout-form').submit();
    });

    
});

