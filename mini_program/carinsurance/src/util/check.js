//验证各种号码 函数名称可见

function isLicenseNumber(number) {
    const regex = /^([\u4e00-\u9fa5][a-zA-Z](([DF](?![a-zA-Z0-9]*[IO])[0-9]{4})|([0-9]{5}[DF])))|([冀豫云辽黑湘皖鲁苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼渝京津沪新京军空海北沈兰济南广成使领A-Z]{1}[a-zA-Z0-9]{5}[a-zA-Z0-9挂学警港澳]{1})$/
    if (!regex.test(number)) {
        return false;
    } else {
        return true;
    }
}

function isPhoneNumber(number) {
    const regex = new Regex(/^1[34578]\d{9}$/)
    if (!regex.test(phone)) {
        return false;
    } else {
        return true;
    }
}

function isBankCard(number) {
    var regex = /^([1-9]{1})(\d{14}|\d{18})$/;
    if (!regex.test(str)) {
        return false;
    } else {
        return true;
    }
}

module.exports = {
    isLicenseNumber: isLicenseNumber,
    isPhoneNumber: isPhoneNumber,
    isBankCard: isBankCard
}