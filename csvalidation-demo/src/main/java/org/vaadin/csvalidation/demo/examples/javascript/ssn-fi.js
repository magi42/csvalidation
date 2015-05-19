var result = "partial";
var day = 0;
var month = 0;
var year = 0;
var millenium = 0;

if (value.length < 2 && value <= 0)
        result = "Invalid date.";

if (value.length >= 2) {
    day = value.substring(0,2);
    if (day < 01 || day > 31)
        result = "Invalid day number, it must be between 01 and 31.";
}

if (value.length >= 4) {
    month = value.substring(2,4);
    if (month < 1 || month > 12)
        result = "Invalid month, it must be between 01 and 12.";
}

if (value.length >= 6) {
    year = value.substring(4,6);
    if (year < 0 || year > 99)
        result = "Invalid year, it must be between 00 and 99.";
}

if (value.length >= 7) {
    milleniumstr = value.substring(6,7);
    if (milleniumstr == "+")
        millenium = 1800;
    else if (milleniumstr == "-")
        millenium = 1900;
    else if (milleniumstr.toUpperCase() == "A")
        millenium = 2000;
    else
        result = "Invalid millenium symbol, it must be one of: +, -, or A.";
}

if (value.length >= 10) {
    personnum = value.substring(7,10);
    if (personnum < 0 || personnum > 999)
        result = "Invalid individual number, it must be between 000-999.";

    if (!((personnum+"000").match(/^[0-9]{3}/)))
    	result = "Non-digits not allowed for person number."
}

if (value.length >= 8 && !((value.substring(7,10)+"000").match(/^[0-9]{3}/)))
	result = "Non-digits not allowed for person number."

if (value.length >= 11) {
    checkmark = value.substring(10,11).toUpperCase();
    checksum = (value.substring(0,6) + value.substring(7,10)) % 31;
    checkmarks = "0123456789ABCDEFHJKLMNPRSTUVWXY";
    if (checkmarks[checksum] == checkmark)
        result = null;
    else
        result = "Invalid checksum.";
}

if (value.length >= 12)
	result = "Too long SSN, must be 12 characters long.";

//The first 6 characters must be digits
if (value.length <= 6 && !((value+"000000").match(/^[0-9]{6}/)))
	result = "Non-digits not allowed for day, month, or year."

result;
