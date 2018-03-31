$(function () {
    // var id = getParameterByName("id");
    $.ajax({
        url: "https://www.howmuchweb.com/carInsurance/api/order/getOrderById",
        dataType: 'jsonp',
        type: "get",
        async: false,
        header: {
            'Access-Control-Allow-origin': 'https://www.howmuchweb.com',
        },
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        data: {
            id: 54
        },
        success: function (data) {
            console.log(data)
            if (data.code == 0) {
                justifyNull(data.data);
                this.orderDetail = data.data;
                $("#pay-qrcode").attr('src', data.data.paycodeurl)
                $("#pay_money").append(data.data.totalPrice / 100)
                $("#pay_name").append(data.data.insureName)
                $("#pay_carplate").append(data.data.licenseNumber)
                $("#pay_number").append(data.data.orderId)
                justifyNull(data.quoteRecord);
                $("#start_time").append(timestampToTime(
                    data.quoteRecord.forceInsuranceStartTime
                ));
                $('#insuranceStartTime').append(timestampToTime(
                    data.quoteRecord.insuranceStartTime
                ));
                var qiangzhiTotalPrice = 0;
                var shangyeTotalPrice = 0;
                var totalPrice = 0;
                data.quoteRecord.qiangzhiList.map(function (item, index) {
                    qiangzhiTotalPrice = Number(item.insurancePrice) + qiangzhiTotalPrice;
                });
                data.quoteRecord.shangyeList.map((item, index) => {
                    shangyeTotalPrice = Number(item.insurancePrice) + shangyeTotalPrice;
                });
                totalPrice = qiangzhiTotalPrice + shangyeTotalPrice;
                $('#cashback').append(data.data.cashback || 0)
                $('#totalPrice').append(totalPrice)
                $('#qiangzhiTotalPrice').append(qiangzhiTotalPrice)
                $('#shangyeTotalPrice').append(shangyeTotalPrice)
                justifyNull(data.invoiceInfo);
                var html = "";
                for (var item of data.quoteRecord.qiangzhiList) {
                    html += '<div class="detail-line">'
                    +'<span>'+item.insuranceName+'</span>'
                    +'<span>￥'+item.insurancePrice+'</span>'
                    +'</div>';
                }

                document.getElementById('insurance-div').innerHTML = html;


                var html1 = "";
                for (var item of data.quoteRecord.shangyeList) {
                    html1 += '<div class="detail-line">'
                    +'<span>'+item.insuranceName;

                    if(item.compensation){
                        html1 += '<span class="detail-line-info">不计免赔</span>';
                    }
                    if(item.amountStr&&item.insuranceId!=2){
                        html1 += '<span  class="detail-line-info">'+item.amountStr+'</span>';
                    }
                    if(item.amountStr&&item.insuranceId===2){
                        html1 += '<span  class="detail-line-info">'+item.amountStr+'</span>';
                    }
                    html +='</span>'
                    +'<span>'+item.insurancePrice+'</span>'
                    +'</div>';
                   
                }

                document.getElementById('insurance-div2').innerHTML = html1;

            }
        },
        error: function (data) {
            console.log(data);
            console.log("error");
        }
    });

})