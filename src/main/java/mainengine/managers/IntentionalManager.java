package mainengine.managers;

import java.util.HashMap;
import java.util.Map;

import intentional.operator.IntentionalStrategy;
import intentional.operator.IntentionalOperator;
import intentional.operator.IntentionalOperatorFactory;
import intentional.operator.IntentionalOperatorType;
import result.ResultFileMetadata;

public class IntentionalManager implements IBuilder {

    private final IntentionalOperatorFactory operatorFactory = new IntentionalOperatorFactory();

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        String query = "";
        Map<String, Object> params = new HashMap<>();

        if (cto.getInput() instanceof String) {
            query = (String) cto.getInput();
        } else if (cto.getInput() instanceof Map) {
            params = (Map<String, Object>) cto.getInput();
            query = (String) params.getOrDefault("query", "");
        }

        String alias = cto.getCommandAlias().toLowerCase();
        IntentionalOperatorType type = IntentionalOperatorType.fromAlias(alias);
        IntentionalStrategy strategy = IntentionalStrategy.fromAlias(alias);

        IntentionalOperator operator = operatorFactory.build(type, strategy, query, cto.getCubeManager(), params);
        ResultFileMetadata results = IntentionalPipeline.run(
                operator, query, IntentionalProfile.forType(type), cto.getCubeManager());

        return new ResponseDTO(results);
    }

}
