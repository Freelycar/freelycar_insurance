$(function () {
    var id = getParameterByName("id");
    $.ajax({
        url: "api/order/getOrderById",
        dataType: 'json',
        type: "get",
        data: {
            id: id
        },
        success: function (data) {
            console.log(data)
            if (data.code == 0) {
                justifyNull(data.data);
                this.orderDetail = data.data;
                $("#pay-qrcode").attr('src', data.data.paycodeurl)
                $("#pay_money").append(data.data.totalPrice / 100)
                $('#pay_cashback').append(data.quoteRecord.backmoney || 0)
                $("#pay_name").append(data.data.insureName)
                $('#pay_cardid').append(data.data.expressNumber)
                $('#pay_bank').append(data.data.expressCompany)
                $('#effectiveTime').append(data.data.effectiveTime)
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
                shangyeTotalPrice = (shangyeTotalPrice + data.quoteRecord.additionalPrice).toFixed(2)
                totalPrice = (qiangzhiTotalPrice + shangyeTotalPrice + Number(data.quoteRecord.additionalPrice)).toFixed(2);
                $('#cashback').append(data.quoteRecord.backmoney || 0)
                $('#totalPrice').append(totalPrice)
                $('#qiangzhiTotalPrice').append(qiangzhiTotalPrice)
                $('#shangyeTotalPrice').append(shangyeTotalPrice)
                justifyNull(data.invoiceInfo);
                var html = "";
                for (var item of data.quoteRecord.qiangzhiList) {
                    html += '<div class="detail-line">' +
                        '<span>' + item.insuranceName + '</span>' +
                        '<span>￥' + item.insurancePrice + '</span>' +
                        '</div>';
                }
                document.getElementById('insurance-div').innerHTML = html;
                var html1 = "";
                for (var item of data.quoteRecord.shangyeList) {
                    html1 += '<div class="detail-line">' +
                        '<span>' + item.insuranceName;

                    if (item.compensation) {
                        html1 += '<span class="detail-line-info">不计免赔</span>';
                    }
                    if (item.amountStr && item.insuranceId != 2) {
                        html1 += '<span  class="detail-line-info">' + item.amountStr + '</span>';
                    }
                    if (item.amountStr && item.insuranceId === 2) {
                        html1 += '<span  class="detail-line-info">' + item.amountStr + '</span>';
                    }
                    html += '</span>' +
                        '<span>' + item.insurancePrice + '</span>' +
                        '</div>';

                }
                html1 = html1 + '<div class="detail-line">' +
                    '<span>' + 不计免赔险 + '</span>' +
                    '<span>' + data.quoteRecord.additionalPrice + '</span>' +
                    '</div>'
                document.getElementById('insurance-div2').innerHTML = html1;

            }
        },
        error: function (data) {
            console.log(data);
            console.log("error");
        }
    });

})