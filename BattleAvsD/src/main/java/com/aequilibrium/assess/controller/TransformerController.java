package com.aequilibrium.assess.controller;

import com.aequilibrium.assess.entity.Transformer;
import com.aequilibrium.assess.service.TransformerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * TransormerController.java - provided the controller to
 *          the client use
 * @Date  : Dec 7, 2018
 * @Author: Bill Huang
 * Version 1.0
 *
 * common:
 *    1) getAllBranchInfo, provided all information that is included fields
 *       as branchName, latitude, longitude, streetAddress, city, countrySubDivision
 *       country, postCode;
 *    2) getAllBranchLocation, provided all locations for client;
 *    3) getBranchByCity, provided the locations filtered by city name.
 ********************************************************************************/

@RestController
@RequestMapping("/transformer")
@Api(tags = "TransformerController", description = "Transformers resource provided to the front side by REST endpoints.")
public class TransformerController {

    @Autowired
    private TransformerService tfsService;

    /**
     * Create a Transformer
     *
     * @param transformer
     * @return transformer
     */
    @PostMapping(produces = {"application/json;charset=UTF-8"})
    @ApiOperation("Create a Transformer")
    public Transformer createTransformer(@RequestBody Transformer transformer) {
        return tfsService.createTransformer(transformer);
    }

    /**
     * Update a Transformer
     *
     * @param transformer
     * @return transformer
     */
    @PutMapping(produces = {"application/json;charset=UTF-8"})
    @ApiOperation("Update a Transformer")
    public Transformer updateTransformer(@RequestBody Transformer transformer) {
        return tfsService.updateTransformer(transformer);
    }

    /**
     * Delete a Transformer by ID
     *
     * @param id
     * @return true/false
     */
    @DeleteMapping(value = "/deleteById/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("Delete a Transformer by ID")
    public Map<String, Object> deleteTransformerById(@PathVariable("id") Long id) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        boolean deleteSuccess = false;
        deleteSuccess = tfsService.deleteTransformerById(id);
        if (deleteSuccess) {
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * Delete a Transformer by name
     *
     * @param name
     * @return true/false
     */
    @DeleteMapping(value = "/deleteByName/{name}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("Delete a Transformer by name and ignore case")
    public Map<String, Object> deleteTransformerByName(@RequestParam(value = "name") String name) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        boolean deleteSuccess = false;
        deleteSuccess = tfsService.deleteTransformerByName(name);
        if (deleteSuccess) {
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * List a Transformer ID
     *
     * @param id
     * @return transformer
     */
    @GetMapping(value = "/listById/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("List a Transformer by ID")
    public Transformer listTransformer(@PathVariable("id") Long id) {
        return tfsService.getTransformerById(id);
    }

    /**
     * List a Transformer by name
     *
     * @param name
     * @return transformer
     */
    @GetMapping(value = "/listByName/{name}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("List a Transformer by name")
    public Transformer listTransformerByName(@PathVariable("name") String name) {
        return tfsService.getTransformerByName(name);
    }

    /**
     * List all Transformers
     *
     * @param
     * @return
     */
    @GetMapping(value = "/listAll", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("List all Transformers")
    public ArrayList<Transformer> listAllTransformer() {
        return tfsService.getAllTransformers();
    }

    /**
     * Get the battle result
     *
     * @param battles
     * @return Map
     */
    @GetMapping("/result/{battles}")
    @ApiOperation("Get the battles result")
    @ApiResponses(value = {
            @ApiResponse(code = 000, message = "000 Win by Optimus Prime or Predaking"),
            @ApiResponse(code = 001, message = "001 Optimus Prime or Predaking duplicated or face to face etc."),
            @ApiResponse(code = 002, message = "002 Win, opponent run away, all survived"),
            @ApiResponse(code = 003, message = "003 Win, opponent survived only exception for flights"),
            @ApiResponse(code = 004, message = "004 They are tie!")
    })
    public Map<String, Object> getBattlesResult(@PathVariable("battles") int battles) {
        return tfsService.checkTheWinner(battles);
    }

}
