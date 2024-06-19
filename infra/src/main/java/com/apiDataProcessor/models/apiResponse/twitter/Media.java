package com.apiDataProcessor.models.apiResponse.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media {
    public static final String MEDIA_KEY = "media_key";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String ALTERNATE_TEXT = "alt_text";
    public static final String DURATION_MS = "duration_ms";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
    public static final String PREVIEW_IMAGE_URL = "preview_image_url";
    public static final String NON_PUBLIC_METRICS = "non_public_metrics";
    public static final String ORGANIC_METRICS = "organic_metrics";
    public static final String PROMOTED_METRICS = "promoted_metrics";
    public static final String PUBLIC_METRICS = "public_metrics";
    public static final String VARIANTS = "variants";

    @Id
    @JsonProperty(MEDIA_KEY)
    private String mediaKey;

    @JsonProperty(TYPE)
    private String type;

    @JsonProperty(URL)
    private String url;

    @JsonProperty(ALTERNATE_TEXT)
    private String alternateText;

    @JsonProperty(DURATION_MS)
    private Long durationMs;

    @JsonProperty(HEIGHT)
    private Long height;

    @JsonProperty(WIDTH)
    private Long width;

    @JsonProperty(PREVIEW_IMAGE_URL)
    private String previewImageUrl;

    @JsonProperty(NON_PUBLIC_METRICS)
    private Map<String, Long> nonPublicMetrics;

    @JsonProperty(ORGANIC_METRICS)
    private Map<String, Long> organicMetrics;

    @JsonProperty(PROMOTED_METRICS)
    private Map<String, Long> promotedMetrics;

    @JsonProperty(PUBLIC_METRICS)
    private Map<String, Long> publicMetrics;

    @JsonProperty(VARIANTS)
    private Object variants;
}
