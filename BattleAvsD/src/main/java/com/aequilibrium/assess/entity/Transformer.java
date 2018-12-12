package com.aequilibrium.assess.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Transformer {

    @ApiModelProperty(notes = "ID is created automatically, it is unique")
    private Long id;

    @ApiModelProperty(notes = "The transformer's name is not blank")
    @NotBlank(message = "Not blank!")
    @Size(min = 3, max = 15)
    private String name;

    @ApiModelProperty(notes = "Fill type as A/D")
    @NotBlank(message = "Fill type as A/D!")
    @Size(max = 1)
    private String type;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer strength;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer intelligence;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer speed;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer endurance;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer rank;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer courage;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer firepower;

    @ApiModelProperty(notes = "Ranked from 1 to 10")
    @Min(1)
    @Max(10)
    private Integer skill;

    @ApiModelProperty(notes = "It is work field. To hold the overall rating")
    private Integer overallRating;

    @ApiModelProperty(notes = "It is work field. To flag status in the game")
    private String flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getEndurance() {
        return endurance;
    }

    public void setEndurance(Integer endurance) {
        this.endurance = endurance;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getCourage() {
        return courage;
    }

    public void setCourage(Integer courage) {
        this.courage = courage;
    }

    public Integer getFirepower() {
        return firepower;
    }

    public void setFirepower(Integer firepower) {
        this.firepower = firepower;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
