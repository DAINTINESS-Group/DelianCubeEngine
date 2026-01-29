package mainengine.managers;

import java.util.Map;
import chartManagement.ChartManager;
import chartRequestManagement.ChartRequest;
import cubemanager.CubeManager;
import result.ResultFileMetadata;
import chartManagement.utils.ChartResponse;

public class VisualizationManager implements IBuilder {

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        Map<String, Object> params = (Map<String, Object>) cto.getInput();
        String command = cto.getCommandAlias();

        CubeManager cubeManager = cto.getCubeManager();
        ChartManager chartManager = (ChartManager) params.get("chartManager");
        ChartRequest request = (ChartRequest) params.get("chartRequest");
        String schemaName = (String) params.get("schemaName");
        String connectionType = (String) params.get("connectionType");

        if (chartManager == null) {
            chartManager = new ChartManager(cubeManager);
        }

        chartManager.createConnectionWithAnalyzeOperator(request.getQuery(), schemaName, connectionType);
        chartManager.setChartType(request.getChart());
        chartManager.generateQueries();

        Object resultPayload = null;

        switch (command) {
            case "answer_chart_metadata":
                resultPayload = chartManager.executeQueries();
                break;

            case "answer_chart_response":
                resultPayload = chartManager.executeQueriesAndReturnToChartResponse();
                break;

            default:
                throw new IllegalArgumentException("Unknown Visualization Command: " + command);
        }

        ResponseDTO response = new ResponseDTO(true);
        response.setPayload(resultPayload);
        return response;
    }
}