package org.task.controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.task.VacationPayment;

@RestController
public class CalculateController {
    @GetMapping(value = "/calculate", produces = MediaType.APPLICATION_JSON_VALUE)
    public VacationPayment calculate(@RequestParam("averageAnnualIncome") double averageAnnualIncome,
                                     @RequestParam("numOfVacationDays") int numOfVacationDays,
                                     @RequestParam("startDate") String startDate,
                                     @RequestParam("endDate") String endDate){

        return new VacationPayment(averageAnnualIncome, numOfVacationDays, startDate, endDate);
    }


}
