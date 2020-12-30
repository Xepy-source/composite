package com.ccrental.composite.cs.apis.controllers;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.AddBranchResult;
import com.ccrental.composite.cs.apis.services.BranchService;
import com.ccrental.composite.cs.apis.vos.AddBranchVo;
import com.ccrental.composite.cs.apis.vos.GetBranchVo;
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
@RequestMapping("/apis/cs/branch")
public class BranchController {
    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getBranch(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "code", defaultValue = "0") String code) throws SQLException, IOException {

        ArrayList<GetBranchVo> branches = this.branchService.getBranch(code);

        JSONObject resultJSON = new JSONObject();

        if (branches == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetBranchVo branch : branches) {
                JSONObject data = new JSONObject();
                data.put("name", branch.getName());
                data.put("address", branch.getAddress());
                data.put("contact", branch.getContact());
                data.put("operation", branch.getOperation());
                data.put("coordinate_x", branch.getCoordinate_x());
                data.put("coordinate_y", branch.getCoordinate_y());
                data.put("approach_bus", branch.getApproach_bus());
                data.put("approach_subway", branch.getApproach_subway());
                dataArray.put(data);
            }
            resultJSON.put("branches", dataArray);
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void addBranch(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "name", defaultValue = "") String name,
                          @RequestParam(name = "address", defaultValue = "") String address,
                          @RequestParam(name = "contact", defaultValue = "") String contact,
                          @RequestParam(name = "code", defaultValue = "") String code,
                          @RequestParam(name = "operation", defaultValue = "") String operation,
                          @RequestParam(name = "coordinate_x", defaultValue = "") String coordinate_x,
                          @RequestParam(name = "coordinate_y", defaultValue = "") String coordinate_y,
                          @RequestParam(name = "approach_bus", defaultValue = "") String approach_bus,
                          @RequestParam(name = "approach_subway", defaultValue = "") String approach_subway ) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        AddBranchResult addBranchResult;
        if (userVo.getLevel() == 1) {
            AddBranchVo addBranchVo = new AddBranchVo(name, address, contact, code, operation, coordinate_x, coordinate_y, approach_bus, approach_subway);
            if (addBranchVo.isNormalization()) {
                addBranchResult = this.branchService.addBranch(addBranchVo);
            } else {
                addBranchResult = AddBranchResult.NORMALIZATION_FAILURE;
            }
        } else {
            addBranchResult = AddBranchResult.NOT_AUTHORITY;
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", addBranchResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }
}
