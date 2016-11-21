package cc.shanruifeng.functions.udfs.scalar.geographic;

import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import java.util.Map;

/**
 * @author ruifeng.shan
 * @date 2016-07-11
 * @time 17:24
 */
public class GeographicFunctions {
    private static final double xPi = 3.14159265358979324 * 3000.0 / 180.0;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;
    private static final double radius = 6371000.0;

    @ScalarFunction("wgs_distance")
    @Description("WGS84 distance. params: lat1, lng1, lat2, lng2")
    @SqlType(StandardTypes.DOUBLE)
    public static double WGS84Distance(@SqlType(StandardTypes.DOUBLE) double lat1, @SqlType(StandardTypes.DOUBLE) double lng1, @SqlType(StandardTypes.DOUBLE) double lat2, @SqlType(StandardTypes.DOUBLE) double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return radius * c;
    }

    // 火星坐标系(GCJ-02)转百度坐标系(BD-09)
    // 谷歌、高德——>百度
    @ScalarFunction("gcj_to_bd")
    @Description("Convert GCJ-02 to BD-09.")
    @SqlType(StandardTypes.JSON)
    public static Slice GCJ02ToBD09(@SqlType(StandardTypes.DOUBLE) double gcjLat, @SqlType(StandardTypes.DOUBLE) double gcjLng) {
        double x = gcjLng, y = gcjLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * xPi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * xPi);
        double bdLng = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;

        return Slices.utf8Slice(getJsonOfCoordinate(bdLat, bdLng));
    }

    // 百度坐标系(BD-09)转火星坐标系(GCJ-02)
    // 百度——>谷歌、高德
    @ScalarFunction("bd_to_gcj")
    @Description("Convert BD-09 to GCJ-02.")
    @SqlType(StandardTypes.JSON)
    public static Slice BD09ToGCJ02(@SqlType(StandardTypes.DOUBLE) double bdLat, @SqlType(StandardTypes.DOUBLE) double bdLng) {
        double x = bdLng - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * xPi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * xPi);
        double gcjLng = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);

        return Slices.utf8Slice(getJsonOfCoordinate(gcjLat, gcjLng));
    }

    // WGS84转GCJ02(火星坐标系)
    @ScalarFunction("wgs_to_gcj")
    @Description("Convert WGS-84 to GCJ-02.")
    @SqlType(StandardTypes.JSON)
    public static Slice WGS84ToGCJ02(@SqlType(StandardTypes.DOUBLE) double wgsLat, @SqlType(StandardTypes.DOUBLE) double wgsLng) {
        //判断是否在国内，不在国内不做偏移
        if (outOfChina(wgsLat, wgsLng)) {
            return Slices.utf8Slice(getJsonOfCoordinate(wgsLat, wgsLng));
        }

        double[] delta = delta(wgsLat, wgsLng);
        double gcjLat = wgsLat + delta[0];
        double gcjLng = wgsLng + delta[1];

        return Slices.utf8Slice(getJsonOfCoordinate(gcjLat, gcjLng));
    }

    // GCJ02(火星坐标系)转WGS84, 低精度(1-2m)
    @ScalarFunction("gcj_to_wgs")
    @Description("Convert GCJ-02 to WGS-84.")
    @SqlType(StandardTypes.JSON)
    public static Slice GCJ02ToWGS84(@SqlType(StandardTypes.DOUBLE) double gcjLat, @SqlType(StandardTypes.DOUBLE) double gcjLng) {
        //判断是否在国内，不在国内不做偏移
        if (outOfChina(gcjLat, gcjLng)) {
            return Slices.utf8Slice(getJsonOfCoordinate(gcjLat, gcjLng));
        }

        double[] delta = delta(gcjLat, gcjLng);
        double wgsLat = gcjLat - delta[0];
        double wgsLng = gcjLng - delta[1];

        return Slices.utf8Slice(getJsonOfCoordinate(wgsLat, wgsLng));
    }

    // GCJ02(火星坐标系)转WGS84, 高精度(<0.5m)
    @ScalarFunction("gcj_extract_wgs")
    @Description("Convert GCJ-02 to WGS-84.")
    @SqlType(StandardTypes.JSON)
    public static Slice GCJ02ExtractWGS84(@SqlType(StandardTypes.DOUBLE) double gcjLat, @SqlType(StandardTypes.DOUBLE) double gcjLng) {
        double initDelta = 0.01;
        double threshold = 0.000001;
        double dLat = initDelta;
        double dLng = initDelta;
        double mLat = gcjLat - dLat;
        double mLng = gcjLng - dLng;
        double pLat = gcjLat + dLat;
        double pLng = gcjLng + dLng;
        double wgsLat = 0, wgsLng = 0;

        for (int i = 0; i < 30; i++) {
            wgsLat = (mLat + pLat) / 2;
            wgsLng = (mLng + pLng) / 2;

            double[] tmp = wgs2gcj(wgsLat, wgsLng);
            double tmpLat = tmp[0];
            double tmpLng = tmp[1];
            dLat = tmpLat - gcjLat;
            dLng = tmpLng - gcjLng;
            if (Math.abs(dLat) < threshold && Math.abs(dLng) < threshold) {
                return Slices.utf8Slice(getJsonOfCoordinate(wgsLat, wgsLng));
            }

            if (dLat > 0) {
                pLat = wgsLat;
            } else {
                mLat = wgsLat;
            }

            if (dLng > 0) {
                pLng = wgsLng;
            } else {
                mLng = wgsLng;
            }
        }

        return Slices.utf8Slice(getJsonOfCoordinate(wgsLat, wgsLng));
    }

    private static double[] wgs2gcj(double wgsLat, double wgsLng) {
        double[] result = new double[2];
        //判断是否在国内，不在国内不做偏移
        if (outOfChina(wgsLat, wgsLng)) {
            result[0] = wgsLat;
            result[1] = wgsLng;
            return result;
        }

        double[] delta = delta(wgsLat, wgsLng);
        double gcjLat = wgsLat + delta[0];
        double gcjLng = wgsLng + delta[1];

        result[0] = gcjLat;
        result[1] = gcjLng;
        return result;
    }

    private static boolean outOfChina(double lat, double lng) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }

        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }

        return false;
    }

    private static double[] delta(double lat, double lng) {
        double[] delta = new double[2];
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        delta[0] = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        delta[1] = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

        return delta;
    }

    private static double transformLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * Math.PI) + 300.0 * Math.sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;

        return ret;
    }

    private static String getJsonOfCoordinate(double latitude, double longitude) {
        try {
            Map<String, Double> map = Maps.newHashMap();
            map.put("lat", latitude);
            map.put("lng", longitude);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
