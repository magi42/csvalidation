result = null;

if (value.length < 8)
  result = "Password must be at least 8 characters";
else {
  var i = 0;
  var count_uc=0, count_lc=0, count_num=0, count_special=0;
  for (i=0; i<value.length; i++) {
    if (value[i].match(/[0-9]/))
        count_num++;
    else if (value[i].match(/[a-z]/))
        count_lc++;
    else if (value[i].match(/[A-Z]/))
        count_uc++;
    else if (value[i].match(/[^0-9^a-z^A-Z]/))
        count_special++;
  }
  if (count_num < 2)
    result = "Password must have at least 2 numbers";
  if (count_lc < 2)
    result = "Password must have at least 2 lower case characters";
  if (count_uc < 2)
    result = "Password must have at least 2 upper case characters";
  if (count_special < 2)
    result = "Password must have at least 2 special characters";
}

result;
