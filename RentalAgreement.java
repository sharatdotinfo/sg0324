import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RentalAgreement {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
    private static final DecimalFormat currencyFormat = new DecimalFormat("$0.00");

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private Date checkoutDate;
    private Date dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    private static final Map<String, String> toolTypeMap = new HashMap<>();
    private static final Map<String, String> toolBrandMap = new HashMap<>();
    private static final Map<String, Double[]> pricingMap = new HashMap<>();

    static {
        toolTypeMap.put("CHNS", "Chainsaw");
        toolTypeMap.put("LADW", "Ladder");
        toolTypeMap.put("JAKD", "Jackhammer");
        toolTypeMap.put("JAKR", "Jackhammer");

        toolBrandMap.put("CHNS", "Stihl");
        toolBrandMap.put("LADW", "Werner");
        toolBrandMap.put("JAKD", "DeWalt");
        toolBrandMap.put("JAKR", "Ridgid");

        // Pricing table [Weekday, Weekend, Holiday]
        pricingMap.put("CHNS", new Double[]{1.99, 1.99, 0.00, 1.99});
        pricingMap.put("LADW", new Double[]{1.49, 1.99, 1.49, 0.00});
        pricingMap.put("JAKD", new Double[]{2.99, 2.99, 0.00, 2.99});
        pricingMap.put("JAKR", new Double[]{2.99, 2.99, 0.00, 2.99});
    }

    public RentalAgreement(String toolCode, int rentalDays, int discountPercent, Date checkoutDate)
            throws IllegalArgumentException {
        // Check if rental day count is valid
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }

        // Check if discount percent is valid
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        this.toolCode = toolCode;
        this.rentalDays = rentalDays;
        this.discountPercent = discountPercent;
        this.checkoutDate = checkoutDate;

        // Set tool type and brand based on tool code
        this.toolType = toolTypeMap.getOrDefault(toolCode, "Unknown");
        this.toolBrand = toolBrandMap.getOrDefault(toolCode, "Unknown");

        calculateDueDate();
        calculateCharges();
    }

    private void calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DATE, rentalDays);
        dueDate = calendar.getTime();
    }

    private void calculateCharges() {
        // Calculate chargeable days
        // Logic for holidays and weekends can be implemented here
        chargeDays = rentalDays;

        // Get pricing information
        Double[] pricing = pricingMap.getOrDefault(toolCode, new Double[]{0.0, 0.0, 0.0, 0.0});

        // Calculate daily rental charge based on tool type and checkout date
        dailyRentalCharge = calculateDailyRentalCharge(pricing, checkoutDate);

        // Calculate pre-discount charge
        preDiscountCharge = chargeDays * dailyRentalCharge;

        // Calculate discount amount
        discountAmount = (preDiscountCharge * discountPercent) / 100;

        // Calculate final charge
        finalCharge = preDiscountCharge - discountAmount;
    }

    private double calculateDailyRentalCharge(Double[] pricing, Date checkoutDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
        boolean isHoliday = isHoliday(calendar);

        if (isHoliday) {
            return pricing[3];
        } else if (isWeekend) {
            return pricing[2];
        } else {
            return pricing[1];
        }
    }

    private boolean isHoliday(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (month == Calendar.JULY && day == 4) || // Independence Day
               (month == Calendar.SEPTEMBER && day == 1 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY); // Labor Day
    }

    public void printRentalAgreement() {
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + dateFormat.format(checkoutDate));
        System.out.println("Due date: " + dateFormat.format(dueDate));
        System.out.println("Daily rental charge: " + currencyFormat.format(dailyRentalCharge));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + currencyFormat.format(preDiscountCharge));
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: " + currencyFormat.format(discountAmount));
        System.out.println("Final charge: " + currencyFormat.format(finalCharge));
    }

    public static void main(String[] args) {
        // Example usage
        try {
            String toolCode = "LADW";
            int rentalDays = 5;
            int discountPercent = 20;
            Date checkoutDate = new Date(); // Current date

            RentalAgreement agreement = new RentalAgreement(toolCode, rentalDays, discountPercent, checkoutDate);
            agreement.printRentalAgreement();
        } catch (IllegalArgumentException e) {
            System.out.println("Error during checkout: " + e.getMessage());
        }
    }
}
