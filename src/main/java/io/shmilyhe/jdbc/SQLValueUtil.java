package io.shmilyhe.jdbc;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class SQLValueUtil {
	// Using a ConcurrentHashMap as a Set (for Java 5 compatibility)
		static final Map<String, Boolean> driversWithNoSupportForGetParameterType =
				new ConcurrentHashMap<String, Boolean>(1);


		private static final Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap<Class<?>, Integer>(32);

		static {
			/* JDBC 3.0 only - not compatible with e.g. MySQL at present
			javaTypeToSqlTypeMap.put(boolean.class, new Integer(Types.BOOLEAN));
			javaTypeToSqlTypeMap.put(Boolean.class, new Integer(Types.BOOLEAN));
			*/
			javaTypeToSqlTypeMap.put(byte.class, Types.TINYINT);
			javaTypeToSqlTypeMap.put(Byte.class, Types.TINYINT);
			javaTypeToSqlTypeMap.put(short.class, Types.SMALLINT);
			javaTypeToSqlTypeMap.put(Short.class, Types.SMALLINT);
			javaTypeToSqlTypeMap.put(int.class, Types.INTEGER);
			javaTypeToSqlTypeMap.put(Integer.class, Types.INTEGER);
			javaTypeToSqlTypeMap.put(long.class, Types.BIGINT);
			javaTypeToSqlTypeMap.put(Long.class, Types.BIGINT);
			javaTypeToSqlTypeMap.put(BigInteger.class, Types.BIGINT);
			javaTypeToSqlTypeMap.put(float.class, Types.FLOAT);
			javaTypeToSqlTypeMap.put(Float.class, Types.FLOAT);
			javaTypeToSqlTypeMap.put(double.class, Types.DOUBLE);
			javaTypeToSqlTypeMap.put(Double.class, Types.DOUBLE);
			javaTypeToSqlTypeMap.put(BigDecimal.class, Types.DECIMAL);
			javaTypeToSqlTypeMap.put(java.sql.Date.class, Types.DATE);
			javaTypeToSqlTypeMap.put(java.sql.Time.class, Types.TIME);
			javaTypeToSqlTypeMap.put(java.sql.Timestamp.class, Types.TIMESTAMP);
			javaTypeToSqlTypeMap.put(Blob.class, Types.BLOB);
			javaTypeToSqlTypeMap.put(Clob.class, Types.CLOB);
		}


		/**
		 * Derive a default SQL type from the given Java type.
		 * @param javaType the Java type to translate
		 * @return the corresponding SQL type, or {@code null} if none found
		 */
		public static int javaTypeToSqlParameterType(Class<?> javaType) {
			Integer sqlType = javaTypeToSqlTypeMap.get(javaType);
			if (sqlType != null) {
				return sqlType;
			}
			if (Number.class.isAssignableFrom(javaType)) {
				return Types.NUMERIC;
			}
			if (isStringValue(javaType)) {
				return Types.VARCHAR;
			}
			if (isDateValue(javaType) || Calendar.class.isAssignableFrom(javaType)) {
				return Types.TIMESTAMP;
			}
			return Integer.MIN_VALUE;
		}

		/**
		 * Set the value for a parameter. The method used is based on the SQL type
		 * of the parameter and we can handle complex types like arrays and LOBs.
		 * @param ps the prepared statement or callable statement
		 * @param paramIndex index of the parameter we are setting
		 * @param inValue the value to set
		 * @throws SQLException if thrown by PreparedStatement methods
		 */
		public static void setParameterValue(PreparedStatement ps, int paramIndex,  Object inValue)
				throws SQLException {
			//System.out.println(paramIndex+"\t"+inValue);
			setParameterValueInternal(ps, paramIndex, inValue);
		}




		/**
		 * Set the value for a parameter. The method used is based on the SQL type
		 * of the parameter and we can handle complex types like arrays and LOBs.
		 * @param ps the prepared statement or callable statement
		 * @param paramIndex index of the parameter we are setting
		 * @param sqlType the SQL type of the parameter
		 * @param typeName the type name of the parameter
		 * (optional, only used for SQL NULL and SqlTypeValue)
		 * @param scale the number of digits after the decimal point
		 * (for DECIMAL and NUMERIC types)
		 * @param inValue the value to set (plain value or a SqlTypeValue)
		 * @throws SQLException if thrown by PreparedStatement methods
		 * @see SqlTypeValue
		 */
		private static void setParameterValueInternal(PreparedStatement ps, int paramIndex, Object inValue) throws SQLException {
			Object inValueToUse = inValue;
			/*if (logger.isTraceEnabled()) {
				logger.trace("Setting SQL statement parameter value: column index " + paramIndex +
						", parameter value [" + inValueToUse +
						"], value class [" + (inValueToUse != null ? inValueToUse.getClass().getName() : "null") +
						"], SQL type ");
			}*/

			if (inValueToUse == null) {
				setNull(ps, paramIndex);
			}
			else {
				setValue(ps, paramIndex, inValueToUse);
			}
		}

		/**
		 * Set the specified PreparedStatement parameter to null,
		 * respecting database-specific peculiarities.
		 */
		private static void setNull(PreparedStatement ps, int paramIndex) throws SQLException {
	
				boolean useSetObject = false;
				Integer sqlTypeToUse = null;
				DatabaseMetaData dbmd = null;
				String jdbcDriverName = null;
				boolean checkGetParameterType = true;
				if (checkGetParameterType && !driversWithNoSupportForGetParameterType.isEmpty()) {
					try {
						dbmd = ps.getConnection().getMetaData();
						jdbcDriverName = dbmd.getDriverName();
						checkGetParameterType = !driversWithNoSupportForGetParameterType.containsKey(jdbcDriverName);
					}
					catch (Throwable ex) {
						//logger.debug("Could not check connection metadata", ex);
					}
				}
				if (checkGetParameterType) {
					try {
						sqlTypeToUse = ps.getParameterMetaData().getParameterType(paramIndex);
					}
					catch (Throwable ex) {
						//if (logger.isDebugEnabled()) {
						//	logger.debug("JDBC 3.0 getParameterType call not supported - using fallback method instead: " + ex);
						//}
					}
				}
				if (sqlTypeToUse == null) {
					// JDBC driver not compliant with JDBC 3.0 -> proceed with database-specific checks
					sqlTypeToUse = Types.NULL;
					try {
						if (dbmd == null) {
							dbmd = ps.getConnection().getMetaData();
						}
						if (jdbcDriverName == null) {
							jdbcDriverName = dbmd.getDriverName();
						}
						if (checkGetParameterType) {
							driversWithNoSupportForGetParameterType.put(jdbcDriverName, Boolean.TRUE);
						}
						String databaseProductName = dbmd.getDatabaseProductName();
						if (databaseProductName.startsWith("Informix") ||
								jdbcDriverName.startsWith("Microsoft SQL Server")) {
							useSetObject = true;
						}
						else if (databaseProductName.startsWith("DB2") ||
								jdbcDriverName.startsWith("jConnect") ||
								jdbcDriverName.startsWith("SQLServer")||
								jdbcDriverName.startsWith("Apache Derby")) {
							sqlTypeToUse = Types.VARCHAR;
						}
					}
					catch (Throwable ex) {
						//logger.debug("Could not check connection metadata", ex);
					}
				}
				if (useSetObject) {
					ps.setObject(paramIndex, null);
				}
				else {
					ps.setNull(paramIndex, sqlTypeToUse);
				}
			
			
		}

		private static void setValue(PreparedStatement ps, int paramIndex,Object inValue) throws SQLException {

			if (isStringValue(inValue.getClass())) {
					ps.setString(paramIndex, inValue.toString());
				}
				else if (isDateValue(inValue.getClass())) {
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
				}
				else if (inValue instanceof Calendar) {
					Calendar cal = (Calendar) inValue;
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
				}
				else {
					// Fall back to generic setObject call without SQL type specified.
					ps.setObject(paramIndex, inValue);
				}
			
		}

		/**
		 * Check whether the given value can be treated as a String value.
		 */
		private static boolean isStringValue(Class<?> inValueType) {
			// Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
			return (CharSequence.class.isAssignableFrom(inValueType) ||
					StringWriter.class.isAssignableFrom(inValueType));
		}

		/**
		 * Check whether the given value is a {@code java.util.Date}
		 * (but not one of the JDBC-specific subclasses).
		 */
		private static boolean isDateValue(Class<?> inValueType) {
			return (java.util.Date.class.isAssignableFrom(inValueType) &&
					!(java.sql.Date.class.isAssignableFrom(inValueType) ||
							java.sql.Time.class.isAssignableFrom(inValueType) ||
							java.sql.Timestamp.class.isAssignableFrom(inValueType)));
		}

	

		
}
