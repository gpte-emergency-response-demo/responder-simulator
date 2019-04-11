package com.redhat.cajun.navy.responder.simulator;

public class StaticData {


    public static String testJson = "{\n" +
            "  \"id\": \"b90a23d3-0bee-41b1-96f1-56922f8225f0\",\n" +
            "  \"messageType\": \"MissionUpdatedCommand\",\n" +
            "  \"invokingService\": \"MissionService\",\n" +
            "  \"timestamp\": 1554488601720,\n" +
            "  \"body\": {\n" +
            "    \"id\": \"006fc03a-f8eb-40b8-8796-0262d6097159\",\n" +
            "    \"incidentId\": \"98965816-e6eb-4edc-9b85-9a6b6ca474b3\",\n" +
            "    \"responderId\": \"responderId\",\n" +
            "    \"responderStartLat\": 29.789,\n" +
            "    \"responderStartLong\": -95.6332,\n" +
            "    \"incidentLat\": 29.7476,\n" +
            "    \"incidentLong\": -95.3691,\n" +
            "    \"destinationLat\": 29.7576,\n" +
            "    \"destinationLong\": -95.3591,\n" +
            "    \"responderLocationHistory\": null,\n" +
            "    \"status\": \"CREATED\",\n" +
            "    \"route\": {\n" +
            "      \"distance\": 31988.1,\n" +
            "      \"duration\": 1892.8000000000002,\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"distance\": 2.6,\n" +
            "          \"duration\": 5.5,\n" +
            "          \"name\": \"Park Row\",\n" +
            "          \"instruction\": \"Head west on Park Row\",\n" +
            "          \"weight\": 37.7,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.788405,\n" +
            "            \"long\": -95.633189\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 1521.4,\n" +
            "          \"duration\": 114.00000000000001,\n" +
            "          \"name\": \"Park Row\",\n" +
            "          \"instruction\": \"Make a U-turn and continue on Park Row\",\n" +
            "          \"weight\": 180.3,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.788405,\n" +
            "            \"long\": -95.633216\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 338.0,\n" +
            "          \"duration\": 48.599999999999994,\n" +
            "          \"name\": \"North Eldridge Parkway\",\n" +
            "          \"instruction\": \"Turn right onto North Eldridge Parkway\",\n" +
            "          \"weight\": 104.8,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.787386,\n" +
            "            \"long\": -95.617788\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 546.6,\n" +
            "          \"duration\": 30.499999999999996,\n" +
            "          \"name\": \"Katy Freeway Frontage Road\",\n" +
            "          \"instruction\": \"Turn left onto Katy Freeway Frontage Road\",\n" +
            "          \"weight\": 49.5,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.784352,\n" +
            "            \"long\": -95.617898\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 385.5,\n" +
            "          \"duration\": 17.6,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Take the ramp on the left towards I-10 East\",\n" +
            "          \"weight\": 23.2,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.784316,\n" +
            "            \"long\": -95.612236\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 10673.3,\n" +
            "          \"duration\": 414.30000000000007,\n" +
            "          \"name\": \"Katy Freeway (I-10; US 90 East)\",\n" +
            "          \"instruction\": \"Merge left onto I-10\",\n" +
            "          \"weight\": 420.9000000000001,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.784559,\n" +
            "            \"long\": -95.608252\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 486.3,\n" +
            "          \"duration\": 20.8,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Take exit 761 towards Wirt Road\",\n" +
            "          \"weight\": 27.9,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.784066,\n" +
            "            \"long\": -95.497698\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 2931.5,\n" +
            "          \"duration\": 177.4,\n" +
            "          \"name\": \"Katy Freeway Frontage Road\",\n" +
            "          \"instruction\": \"Merge right onto Katy Freeway Frontage Road\",\n" +
            "          \"weight\": 245.40000000000003,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.783618,\n" +
            "            \"long\": -95.492688\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 1068.9,\n" +
            "          \"duration\": 48.5,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Take the ramp on the left towards I-10 East\",\n" +
            "          \"weight\": 54.900000000000006,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.783622,\n" +
            "            \"long\": -95.462369\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 8463.1,\n" +
            "          \"duration\": 364.7,\n" +
            "          \"name\": \"Katy Freeway (I-10; US 90)\",\n" +
            "          \"instruction\": \"Merge left onto I-10\",\n" +
            "          \"weight\": 371.79999999999995,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.779563,\n" +
            "            \"long\": -95.452603\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 240.9,\n" +
            "          \"duration\": 34.8,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Take exit 768B on the left towards I-45 South: Galveston\",\n" +
            "          \"weight\": 40.3,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.773936,\n" +
            "            \"long\": -95.368035\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 1154.5,\n" +
            "          \"duration\": 71.2,\n" +
            "          \"name\": \"I-45 South\",\n" +
            "          \"instruction\": \"Merge left onto I-45 South\",\n" +
            "          \"weight\": 77.19999999999999,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.772132,\n" +
            "            \"long\": -95.366655\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 432.2,\n" +
            "          \"duration\": 21.2,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Take exit 47D towards Dallas Street\",\n" +
            "          \"weight\": 28.2,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.763482,\n" +
            "            \"long\": -95.370338\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 1081.7,\n" +
            "          \"duration\": 62.4,\n" +
            "          \"name\": \"Pierce Elevated\",\n" +
            "          \"instruction\": \"Continue onto Pierce Elevated\",\n" +
            "          \"weight\": 76.8,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.761611,\n" +
            "            \"long\": -95.374144\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 809.2,\n" +
            "          \"duration\": 136.70000000000002,\n" +
            "          \"name\": \"Pierce Street\",\n" +
            "          \"instruction\": \"Turn left onto Pierce Street\",\n" +
            "          \"weight\": 257.0,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.75207,\n" +
            "            \"long\": -95.375137\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 77.9,\n" +
            "          \"duration\": 15.0,\n" +
            "          \"name\": \"Caroline Street\",\n" +
            "          \"instruction\": \"Turn right onto Caroline Street\",\n" +
            "          \"weight\": 28.7,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.747922,\n" +
            "            \"long\": -95.368254\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 49.4,\n" +
            "          \"duration\": 22.2,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Turn right\",\n" +
            "          \"weight\": 89.3,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.74734,\n" +
            "            \"long\": -95.368703\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 0.0,\n" +
            "          \"duration\": 0.0,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"You have arrived at your 1st destination\",\n" +
            "          \"weight\": 0.0,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.747578,\n" +
            "            \"long\": -95.369117\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 49.8,\n" +
            "          \"duration\": 27.8,\n" +
            "          \"name\": \"\",\n" +
            "          \"instruction\": \"Head northwest\",\n" +
            "          \"weight\": 126.8,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.747578,\n" +
            "            \"long\": -95.369117\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 1294.6,\n" +
            "          \"duration\": 183.20000000000002,\n" +
            "          \"name\": \"San Jacinto Street\",\n" +
            "          \"instruction\": \"Turn left onto San Jacinto Street\",\n" +
            "          \"weight\": 387.29999999999995,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.74786,\n" +
            "            \"long\": -95.369372\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 202.6,\n" +
            "          \"duration\": 35.4,\n" +
            "          \"name\": \"Rusk Street\",\n" +
            "          \"instruction\": \"Turn right onto Rusk Street\",\n" +
            "          \"weight\": 92.4,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.757403,\n" +
            "            \"long\": -95.362002\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 178.0,\n" +
            "          \"duration\": 41.0,\n" +
            "          \"name\": \"Austin Street\",\n" +
            "          \"instruction\": \"Turn left onto Austin Street\",\n" +
            "          \"weight\": 53.1,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.756362,\n" +
            "            \"long\": -95.36028\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"distance\": 0.0,\n" +
            "          \"duration\": 0.0,\n" +
            "          \"name\": \"Austin Street\",\n" +
            "          \"instruction\": \"You have arrived at your destination, on the right\",\n" +
            "          \"weight\": 0.0,\n" +
            "          \"loc\": {\n" +
            "            \"lat\": 29.757685,\n" +
            "            \"long\": -95.359243\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
