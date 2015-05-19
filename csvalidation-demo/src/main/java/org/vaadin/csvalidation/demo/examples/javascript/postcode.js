if (value >= 0 && value < 10000)
	"partial";
else if (value >= 10000 && value <= 99999)
	null;
else
	"Postal Code must be a 5-digit number between 10000 and 99999";
