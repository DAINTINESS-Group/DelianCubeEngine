package mainengine.managers;

import cubemanager.cubebase.CubeQuery;
public class UsabilityManager {

    //singleton
    private static UsabilityManager usabilityManager;

    //factory
    public static UsabilityManager getInstance()
    {
        if (usabilityManager == null) {
            usabilityManager = new UsabilityManager();
        }
        return usabilityManager;
    }
    public boolean checkUsability(){
        return true;
    }

    //result
    public String executeCubeQueryWithUsability(CubeQuery currentCubeQuery){
        return null;
    }
}
