package cc.shanruifeng.functions.udfs.scalar.geographic;

import com.alibaba.fastjson.JSON;
import com.facebook.presto.metadata.FunctionListBuilder;

import io.airlift.slice.Slice;

import org.junit.Test;

import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertTrue;

/**
 * @author ruifeng.shan
 * @date 2016-07-12
 * @time 15:08
 */
public class GeographicFunctionsTest {
  @Test
  public void testFunctionCreate() throws Exception {
    FunctionListBuilder builder = new FunctionListBuilder();
    builder.scalars(GeographicFunctions.class);
  }

  @Test
  public void test_gcj_to_bd() throws Exception {
    Slice gcj = GeographicFunctions.GCJ02ToBD09(new Double(22.499374931276), new Double(113.9431826306));
    HashMap<String, Double> testresult = new HashMap<String, Double>();
    testresult.put("lng", 113.94974983525127);
    testresult.put("lat", 22.505037963671622);
    String resesult = JSON.toJSONString(testresult);

    assertTrue(gcj.toString(UTF_8).equals(resesult.toString()));
  }
}
