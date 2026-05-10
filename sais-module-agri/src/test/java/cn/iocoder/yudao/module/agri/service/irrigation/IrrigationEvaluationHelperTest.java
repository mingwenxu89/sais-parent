package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests IrrigationEvaluationHelper.applyRules() with different parameter combinations.
 *
 * Decision logic (RAIN_SKIP_THRESHOLD = 5mm):
 *   soilMoisture >= moistureMin              → NO_ACTION
 *   soilMoisture <  moistureMin, rain >= 5   → SKIP
 *   soilMoisture <  moistureMin, rain <  5   → IRRIGATE
 */
class IrrigationEvaluationHelperTest {

    // Instantiate directly — applyRules has no dependencies
    private final IrrigationEvaluationHelper helper = new IrrigationEvaluationHelper();

    // ── applyRules decision matrix ────────────────────────────────────────────

    static Stream<Arguments> applyRulesCases() {
        return Stream.of(
            // description,          moisture, min,  optimal, rainfall, expectedDecision
            Arguments.of("adequate moisture → NO_ACTION",
                         "55.0", "40.0", "60.0", "0.0",  "NO_ACTION"),
            Arguments.of("moisture exactly at min → NO_ACTION",
                         "40.0", "40.0", "60.0", "3.0",  "NO_ACTION"),
            Arguments.of("below min, no rain → IRRIGATE",
                         "30.0", "40.0", "60.0", "0.0",  "IRRIGATE"),
            Arguments.of("below min, rain just below threshold (4.9mm) → IRRIGATE",
                         "30.0", "40.0", "60.0", "4.9",  "IRRIGATE"),
            Arguments.of("below min, rain exactly at threshold (5mm) → SKIP",
                         "30.0", "40.0", "60.0", "5.0",  "SKIP"),
            Arguments.of("below min, heavy rain → SKIP",
                         "20.0", "40.0", "60.0", "20.0", "SKIP"),
            Arguments.of("severely dry, no rain → IRRIGATE",
                         "10.0", "40.0", "60.0", "1.0",  "IRRIGATE"),
            Arguments.of("moisture just below min → IRRIGATE",
                         "39.9", "40.0", "60.0", "0.0",  "IRRIGATE"),
            Arguments.of("zero moisture, no rain → IRRIGATE",
                         "0.0",  "40.0", "60.0", "0.0",  "IRRIGATE"),
            Arguments.of("adequate moisture despite rain forecast → NO_ACTION",
                         "50.0", "40.0", "60.0", "10.0", "NO_ACTION")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("applyRulesCases")
    @DisplayName("applyRules decision matrix")
    void applyRules_decision(String description,
                             String moisture, String min, String optimal, String rainfall,
                             String expectedDecision) {
        AiDecisionResultVO ctx = buildCtx(moisture, min, optimal, rainfall);

        AiDecisionResultVO result = helper.applyRules(ctx);

        assertEquals(expectedDecision, result.getDecision(),
                "Expected " + expectedDecision + " but got " + result.getDecision()
                + " — reason: " + result.getReason());
    }

    // ── Reason content checks ─────────────────────────────────────────────────

    @ParameterizedTest(name = "{0}")
    @MethodSource("reasonContentCases")
    @DisplayName("applyRules reason includes key values")
    void applyRules_reason(String description, String moisture, String min, String optimal,
                           String rainfall, String expectedInReason) {
        AiDecisionResultVO result = helper.applyRules(buildCtx(moisture, min, optimal, rainfall));

        assertTrue(result.getReason().contains(expectedInReason),
                "Reason should contain '" + expectedInReason + "' but was: " + result.getReason());
    }

    static Stream<Arguments> reasonContentCases() {
        return Stream.of(
            Arguments.of("IRRIGATE reason mentions optimal and duration",
                         "30.0", "40.0", "60.0", "0.0", "min."),
            Arguments.of("SKIP reason mentions rain threshold",
                         "30.0", "40.0", "60.0", "5.0", "5.0"),
            Arguments.of("NO_ACTION reason mentions acceptable range",
                         "55.0", "40.0", "60.0", "0.0", "within acceptable range")
        );
    }

    // ── estimateDuration ──────────────────────────────────────────────────────

    static Stream<Arguments> durationCases() {
        return Stream.of(
            // current, optimal, expectedDuration
            Arguments.of("small deficit → 15 min (floor)",    "39.0", "40.0", 15),
            Arguments.of("10% deficit → 20 min",              "30.0", "40.0", 20),
            Arguments.of("25% deficit → 50 min",              "15.0", "40.0", 50),
            Arguments.of("large deficit → 120 min (ceiling)", "0.0",  "70.0", 120)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("durationCases")
    @DisplayName("estimateDuration clamps to [15, 120]")
    void estimateDuration(String description, String current, String optimal, int expected) {
        int duration = helper.estimateDuration(new BigDecimal(current), new BigDecimal(optimal));

        assertEquals(expected, duration,
                "Expected duration " + expected + " but got " + duration);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private AiDecisionResultVO buildCtx(String moisture, String min, String optimal, String rainfall) {
        AiDecisionResultVO ctx = new AiDecisionResultVO();
        ctx.setFieldId(1L);
        ctx.setFieldName("Test Field");
        ctx.setStageName("Vegetative");
        ctx.setCurrentMoisture(new BigDecimal(moisture));
        ctx.setMoistureMin(new BigDecimal(min));
        ctx.setMoistureMax(new BigDecimal("80.0"));
        ctx.setMoistureOptimal(new BigDecimal(optimal));
        ctx.setTomorrowRainfall(new BigDecimal(rainfall));
        return ctx;
    }
}
