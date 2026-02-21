package com.fis.boportalservice.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserInfo {

    @JsonProperty("INDI")
    public INDI INDI;

    @JsonProperty("CORP")
    public List<CORP> CORP;

    @JsonProperty("extend")
    public Extend extend;

    @JsonProperty("iat")
    public Integer iat;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class INDI {
        @JsonProperty("userId")
        public String userId;

        @JsonProperty("clientNo")
        public String clientNo;

        @JsonProperty("KYCLevel")
        public String kYCLevel;

        @JsonProperty("customerType")
        public String customerType;

        @JsonProperty("ekycLevel")
        public String ekycLevel;

        @JsonProperty("userRole")
        public String userRole;

        @JsonProperty("featureList")
        public String featureList;

        @JsonProperty("domain")
        public String domain;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CORP {
        @JsonProperty("userID")
        public String userID;

        @JsonProperty("clientNo")
        public String clientNo;

        @JsonProperty("featureList")
        public String featureList;

        @JsonProperty("domain")
        public String domain;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extend {}
}
