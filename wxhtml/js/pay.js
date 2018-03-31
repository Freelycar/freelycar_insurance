~(function () {
    var id = getParameterByName("id");
    $.ajax({
        url: "https://www.howmuchweb.com/carInsurance/api/order/getOrderById",
        dataType: "json",
        type: "get",
        data: {
            id: id
        },
        success: function (data) {
            console.log(data)
            if (data.code == 0) {

            }
        },
        error: function (data) {
            console.log(data);
            console.log("error");
        }
    });

})