package com.ccrental.composite.cs.apis.controllers;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.AddRepairResult;
import com.ccrental.composite.cs.apis.services.RepairService;
import com.ccrental.composite.cs.apis.vos.AddRepairVo;
import com.ccrental.composite.cs.apis.vos.GetRepairVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/apis/cs/repair")
public class RepairController {
    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getRepair(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "code", defaultValue = "0") String code) throws SQLException, IOException {

        ArrayList<GetRepairVo> repairs = this.repairService.getRepair(code);

        JSONObject resultJSON = new JSONObject();
        if (repairs == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetRepairVo repair : repairs) {
                JSONObject data = new JSONObject();
                data.put("name", repair.getName());
                data.put("address", repair.getAddress());
                data.put("contact", repair.getContact());
                data.put("coordinate_x", repair.getCoordinate_x());
                data.put("coordinate_y", repair.getCoordinate_y());
                dataArray.put(data);
            }
            resultJSON.put("repairs", dataArray);
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void addRepair(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "name", defaultValue = "") String name,
                          @RequestParam(name = "address", defaultValue = "") String address,
                          @RequestParam(name = "contact", defaultValue = "") String contact,
                          @RequestParam(name = "code", defaultValue = "") String code,
                          @RequestParam(name = "coordinate_x", defaultValue = "") String coordinate_x,
                          @RequestParam(name = "coordinate_y", defaultValue = "") String coordinate_y) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        AddRepairResult addRepairResult;
        if (userVo.getLevel() == 1) {
            AddRepairVo addRepairVo = new AddRepairVo(name, address, contact, code, coordinate_x, coordinate_y);
            if (addRepairVo.isNormalization()) {
                addRepairResult = this.repairService.addRepair(addRepairVo);
            } else {
                addRepairResult = AddRepairResult.NORMALIZATION_FAILURE;
            }
        } else {
            addRepairResult = AddRepairResult.NOT_AUTHORITY;
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", addRepairResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }
}
