package bspl.einvoice.eiew.Models;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ParameterMetaData;

public final class DataLayerParameterCache {

    // Since this class provides only static methods, make the default constructor private to prevent 
    // instances from being created with "new DataLayerParameterCache()".
    private DataLayerParameterCache() { }

    private static ConcurrentHashMap<String, OracleParameter[]> paramCache = new ConcurrentHashMap<>();

    /**
     * Resolve at run time the appropriate set of OracleParameters for a stored procedure
     * @param connectionString a valid connection string for a OracleConnection
     * @param spName the name of the stored procedure
     * @param includeReturnValueParameter whether or not to include their return value parameter
     * @return an array of OracleParameters
     */
    private static OracleParameter[] DiscoverSpParameterSet(String connectionString, String spName, boolean includeReturnValueParameter) throws SQLException {
        try (Connection cn = DriverManager.getConnection(connectionString);
             CallableStatement cmd = cn.prepareCall("{call " + spName + "}")) {
            cn.setAutoCommit(false);
            ParameterMetaData pmd = cmd.getParameterMetaData();
            int parameterCount = pmd.getParameterCount();
            OracleParameter[] discoveredParameters = new OracleParameter[parameterCount];

            for (int i = 0; i < parameterCount; i++) {
                discoveredParameters[i] = new OracleParameter(pmd.getParameterType(i + 1), pmd.getParameterTypeName(i + 1));
            }

            if (!includeReturnValueParameter) {
                // Remove the return value parameter if necessary
                // This part is commented out in the original code
            }

            return discoveredParameters;
        }
    }

    // Deep copy of cached OracleParameter array
    private static OracleParameter[] CloneParameters(OracleParameter[] originalParameters) {
        OracleParameter[] clonedParameters = new OracleParameter[originalParameters.length];

        for (int i = 0; i < originalParameters.length; i++) {
            clonedParameters[i] = originalParameters[i].clone();
        }

        return clonedParameters;
    }

    /**
     * Add parameter array to the cache
     * @param connectionString a valid connection string for a OracleConnection
     * @param commandText the stored procedure name or PL/SQL command
     * @param commandParameters an array of OracleParameters to be cached
     */
    public static void CacheParameterSet(String connectionString, String commandText, OracleParameter... commandParameters) {
        String hashKey = CreateHashKey(connectionString, commandText);
        paramCache.put(hashKey, commandParameters);
    }

    /**
     * Retrieve a parameter array from the cache
     * @param connectionString a valid connection string for a OracleConnection
     * @param commandText the stored procedure name or PL/SQL command
     * @return an array of OracleParameters
     */
    public static OracleParameter[] GetCachedParameterSet(String connectionString, String commandText) {
        String hashKey = CreateHashKey(connectionString, commandText);
        OracleParameter[] cachedParameters = paramCache.get(hashKey);

        if (cachedParameters == null) {
            return null;
        } else {
            return CloneParameters(cachedParameters);
        }
    }

    /**
     * Retrieves the set of OracleParameters appropriate for the stored procedure
     * @param connectionString a valid connection string for a OracleConnection
     * @param spName the name of the stored procedure
     * @return an array of OracleParameters
     */
    public static OracleParameter[] GetSpParameterSet(String connectionString, String spName) throws SQLException {
        return GetSpParameterSet(connectionString, spName, false);
    }

    /**
     * Retrieves the set of OracleParameters appropriate for the stored procedure
     * @param connectionString a valid connection string for a OracleConnection
     * @param spName the name of the stored procedure
     * @param includeReturnValueParameter a bool value indicating whether the return value parameter should be included in the results
     * @return an array of OracleParameters
     */
    public static OracleParameter[] GetSpParameterSet(String connectionString, String spName, boolean includeReturnValueParameter) throws SQLException {
        String hashKey = CreateHashKey(connectionString, spName, includeReturnValueParameter);
        OracleParameter[] cachedParameters = paramCache.get(hashKey);

        if (cachedParameters == null) {
            cachedParameters = DiscoverSpParameterSet(connectionString, spName, includeReturnValueParameter);
            paramCache.put(hashKey, cachedParameters);
        }

        return CloneParameters(cachedParameters);
    }

    private static String CreateHashKey(String connectionString, String spName, boolean includeReturnValueParameter) {
        return connectionString + ":" + spName + (includeReturnValueParameter ? ":include ReturnValue Parameter" : "");
    }

    private static String CreateHashKey(String connectionString, String commandText) {
        return connectionString + ":" + commandText;
    }
}
