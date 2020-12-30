package com.ccrental.composite.rental.apis.controllers;

import com.ccrental.composite.common.utillity.Constant;
import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.rental.apis.containers.CarListContainer;
import com.ccrental.composite.rental.apis.containers.RentalResultContainer;
import com.ccrental.composite.rental.apis.enums.RentalGetResult;
import com.ccrental.composite.rental.apis.services.RentalService;
import com.ccrental.composite.rental.apis.vos.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;


@RestController
@RequestMapping(
        value = "/apis/rental/",
        method = {RequestMethod.GET, RequestMethod.POST},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @RequestMapping(value = "get")
    public String getRental(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(name = "from_date", defaultValue = "") String strFromDate,
                            @RequestParam(name = "from_time", defaultValue = "") String strFromTime,
                            @RequestParam(name = "to_date", defaultValue = "") String strToDate,
                            @RequestParam(name = "to_time", defaultValue = "") String strToTime,
                            @RequestParam(name = "branch", defaultValue = "") String strBranch
    ) throws SQLException, ParseException {
        // 시간입력
        Date fromDate = Converter.dateParsing(strFromDate);
        Time fromTime = Converter.timeParsing(strFromTime);
        Date toDate = Converter.dateParsing(strToDate);
        Time toTime = Converter.timeParsing(strToTime);

        InputVo inputVo = new InputVo(fromDate, fromTime, toDate, toTime, strBranch);

        JSONObject jsonResponse = new JSONObject();
        CarListContainer carListContainer = this.rentalService.getCarList(inputVo);
        if (fromDate.after(toDate)) {
            jsonResponse.put("date", Constant.result.JSON_DATE_RESULT_FAIL.toLowerCase());
        } else {
            jsonResponse.put("date", Constant.result.JSON_DATE_RESULT_OK.toLowerCase());
            JSONArray jsonCars = new JSONArray();
            if (carListContainer.getRentalGetResult() == RentalGetResult.SUCCESS) {
                for (RentalVo rental : carListContainer.getCars()) {
                    JSONObject jsonRental = new JSONObject();
                    jsonRental.put("branchDiv", rental.getBranchDiv());
                    jsonRental.put("branchIndex", rental.getBranchIndex());
                    jsonRental.put("carIndex", rental.getCarIndex());
                    jsonRental.put("carName", rental.getCarName());
                    jsonRental.put("carType", rental.getCarType());
                    if (rental.isAvailable()) {
                        jsonRental.put("available", Constant.result.JSON_ENTRY_RESULT_OK);
                    } else {
                        jsonRental.put("available", Constant.result.JSON_ENTRY_RESULT_FAIL);
                    }
                    jsonCars.put(jsonRental);
                }
                jsonResponse.put("result", carListContainer.getRentalGetResult().name().toLowerCase());
                jsonResponse.put("cars", jsonCars);
            }
        }
        return jsonResponse.toString(4);
    }

    @RequestMapping(value = "add")
    public void getRental(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "from_date", defaultValue = "") String strFromDate,
                          @RequestParam(name = "from_time", defaultValue = "") String strFromTime,
                          @RequestParam(name = "to_date", defaultValue = "") String strToDate,
                          @RequestParam(name = "to_time", defaultValue = "") String strToTime,
                          @RequestParam(name = "branch", defaultValue = "") String strBranch,
                          @RequestParam(name = "car", defaultValue = "") String strCar
    ) throws SQLException, ParseException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        JSONObject jsonResponse = new JSONObject();

        if (userVo == null) {
            jsonResponse.put("result", "Need to Log in");
        } else {
            // 시간입력
            Date fromDate = Converter.dateParsing(strFromDate);
            Time fromTime = Converter.timeParsing(strFromTime);
            Date toDate = Converter.dateParsing(strToDate);
            Time toTime = Converter.timeParsing(strToTime);
            int branchIndex = Converter.stringToInt(strBranch, -1);
            int carIndex = Converter.stringToInt(strCar, -1);

            AddRentalVo addRentalVo = new AddRentalVo(fromDate, fromTime, toDate, toTime, branchIndex, carIndex, userVo.getIndex());

            // 출력
            RentalResultContainer rentalResultContainer = this.rentalService.getRental(addRentalVo);
            JSONArray jsonCounts = new JSONArray();
            if (rentalResultContainer.getRentalGetResult() == RentalGetResult.SUCCESS) {
                for (CountVo count : rentalResultContainer.getCounts()) {
                    JSONObject jsonRental = new JSONObject();
                    jsonRental.put("branchIndex", count.getBranchIndex());
                    jsonRental.put("carIndex", count.getCarIndex());
                    jsonRental.put("carName", count.getCarName());
                    jsonRental.put("carType", count.getCarType());
                    jsonRental.put("available", Constant.result.JSON_ENTRY_RESULT_OK);
                    jsonCounts.put(jsonRental);
                }
                jsonResponse.put("result", rentalResultContainer.getRentalGetResult().name().toLowerCase());
                jsonResponse.put("cars", jsonCounts);
            } else if (rentalResultContainer.getRentalGetResult() == RentalGetResult.SOLDOUT) {
                for (CountVo count : rentalResultContainer.getCounts()) {
                    JSONObject jsonRental = new JSONObject();
                    jsonRental.put("branchIndex", count.getBranchIndex());
                    jsonRental.put("carIndex", count.getCarIndex());
                    jsonRental.put("carName", count.getCarName());
                    jsonRental.put("carType", count.getCarType());
                    jsonRental.put("available", Constant.result.JSON_ENTRY_RESULT_FAIL);
                    jsonCounts.put(jsonRental);
                }
                jsonResponse.put("result", rentalResultContainer.getRentalGetResult().name().toLowerCase());
                jsonResponse.put("cars", jsonCounts);
            } else {
                jsonResponse.put("result", RentalGetResult.RENTAL_FAILED.name().toLowerCase());
            }
        }
        response.getWriter().print(jsonResponse.toString(4));
    }

    @RequestMapping(value = "get-branch-info")
    public String getBranchInfoGet(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "index", defaultValue = "") String indexString) throws
            SQLException {
        int index = Converter.stringToInt(indexString, -1);
        JSONObject jsonResponse = new JSONObject();
        if (index > -1) {
            String branchName = this.rentalService.getBranchInfo(index);
            if (branchName != null) {
                jsonResponse.put("name", branchName);
            }
        }
        return jsonResponse.toString(4);
    }

    @RequestMapping(value = "get-car-info")
    public String getCarInfoGet(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "index", defaultValue = "") String indexString) throws
            SQLException {
        int index = Converter.stringToInt(indexString, -1);
        JSONObject jsonResponse = new JSONObject();
        if (index > -1) {
            CarRentInfoVo carRentInfoVo = this.rentalService.getCarInfo(index);
            if (carRentInfoVo != null) {
                jsonResponse.put("name", carRentInfoVo.getName());
                jsonResponse.put("price", carRentInfoVo.getPrice());
                jsonResponse.put("ins", carRentInfoVo.getIns());
            }
        }
        return jsonResponse.toString(4);
    }

    @RequestMapping(value = "book")
    public void getMyRental(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        JSONObject jsonResponse = new JSONObject();
        System.out.println(userVo.getIndex());
        if (userVo == null) {
            jsonResponse.put("result", "Need to Log in");
        } else {
            ArrayList<MyRentalVo> myRentals = this.rentalService.getMyRental(userVo.getIndex());
            if (myRentals == null) {
                jsonResponse.put("result", "My Rental Info Empty");
            } else {
                jsonResponse.put("result", "success");
                JSONArray myRentalInfo = new JSONArray();
                for (int i = 0; i < myRentals.size(); i++) {
                    JSONObject rentalInfo = new JSONObject();
                    rentalInfo.put("fromDate", myRentals.get(i).getFromDate());
                    rentalInfo.put("fromTime", myRentals.get(i).getFromTime());
                    rentalInfo.put("toDate", myRentals.get(i).getToDate());
                    rentalInfo.put("toTime", myRentals.get(i).getToTime());
                    rentalInfo.put("branchIndex", myRentals.get(i).getBranchIndex());
                    rentalInfo.put("carIndex", myRentals.get(i).getCarIndex());
                    rentalInfo.put("userIndex", myRentals.get(i).getUserIndex());
                    myRentalInfo.put(rentalInfo);
                }
                jsonResponse.put("rentalInfo", myRentalInfo);
            }
        }
        response.getWriter().print(jsonResponse.toString(4));
        response.getWriter().close();
    }
}