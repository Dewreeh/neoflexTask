package org.task;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter @JsonIgnoreProperties({"averageAnnualIncome", "numOfVacationDays", "startDate", "endDate"})
public class VacationPayment {
    private final BigDecimal vacationPayment;
    private final BigDecimal averageAnnualIncome;
    private final BigDecimal numOfVacationDays;
    private final LocalDate startDate;
    private final LocalDate endDate;
    public VacationPayment(double averageAnnualIncome, int numOfVacationDays, String startDate, String endDate){

        //Для более точного расчета переводим значения в BigDecimal
        this.averageAnnualIncome = BigDecimal.valueOf(averageAnnualIncome);
        this.numOfVacationDays = BigDecimal.valueOf(numOfVacationDays);

        //Кастим дату из String в объект LocalDate
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);

        //получаем количество неоплачиваемых дней
        BigDecimal countOfUnpaidDays = getUnpaidDaysCount();

        //считаем среднюю дневную зарплату (делим среднемесячную за год на 29.3)
        BigDecimal averageIncomePerDay = this.averageAnnualIncome.divide(new BigDecimal("29.3"), 2, RoundingMode.HALF_EVEN);

        //Считаем вычет из отпускных за неоплачиваемые дни (количество таких денй умножаем на средний дневной заработок)
        BigDecimal deduction = averageIncomePerDay.multiply(countOfUnpaidDays);

        //считаем итоговую выплату - средний дневной доход умножаем на количество дней отпуска и вычитаем сумму за неоплачиваемые дни
        this.vacationPayment = averageIncomePerDay.multiply(this.numOfVacationDays).subtract(deduction);
    }

    //Находим количество неоплачиваемых дней(выходных) в отпуске
    private BigDecimal getUnpaidDaysCount(){
        //Берём дату начала отпуска и увеличиваем её на 1 день в цикле, проходя через все дни отпуска.
        //Для каждого дня находим день недели. Если это суббота или воскресенье, увеличиваем счетчик неоплачиваемых дней на 1
        LocalDate tmp = this.startDate;
        int countOfUnpaidDays = 0;
        while(!tmp.isEqual(this.endDate)){
            tmp = tmp.plusDays(1);
            DayOfWeek dayOfWeek = tmp.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
                countOfUnpaidDays++;
            }
        }
        return BigDecimal.valueOf(countOfUnpaidDays);
    }
}
