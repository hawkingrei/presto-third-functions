package cc.shanruifeng.functions.udfs.scalar.json;


import com.facebook.presto.operator.scalar.JsonFunctions;
import com.facebook.presto.operator.scalar.JsonPath;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.block.BlockBuilderStatus;
import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import com.facebook.presto.type.JsonPathType;

import io.airlift.slice.Slice;

import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

/**
 * @author ruifeng.shan
 * @date 2016-07-21
 * @time 15:43
 */
public class JsonArrayExtractFunction {



  @Description("extract json array value by given jsonPath.")
  @ScalarFunction("json_array_extract")
  @SqlType("array(varchar)")
  public static Block jsonArrayExtract(@SqlType(StandardTypes.VARCHAR) Slice json, @SqlType(JsonPathType.NAME) JsonPath jsonPath) {
    Long length = JsonFunctions.jsonArrayLength(json);
    if (length == null) {
      return null;
    }

    BlockBuilder blockBuilder = VARCHAR.createBlockBuilder(new BlockBuilderStatus(), length.intValue());

    for (int i = 0; i < length; i++) {
      Slice content = JsonFunctions.varcharJsonArrayGet(json, i);
      Slice result = JsonFunctions.varcharJsonExtract(content, jsonPath);
      VARCHAR.writeSlice(blockBuilder, result);
      //appendToBlockBuilder(VARCHAR, result, blockBuilder);
    }
    return blockBuilder.build();
  }


  @Description("extract json array value by given jsonPath.")
  @ScalarFunction("json_array_extract_scalar")
  @SqlType("array(varchar)")
  public static Block jsonArrayExtractScalar(@SqlType(StandardTypes.VARCHAR) Slice json, @SqlType(JsonPathType.NAME) JsonPath jsonPath) {
    Long length = JsonFunctions.jsonArrayLength(json);
    if (length == null) {
      return null;
    }
    BlockBuilder blockBuilder = VARCHAR.createBlockBuilder(new BlockBuilderStatus(), length.intValue());
    for (int i = 0; i < length; i++) {
      Slice content = JsonFunctions.varcharJsonArrayGet(json, i);
      Slice result = JsonFunctions.varcharJsonExtractScalar(content, jsonPath);
      VARCHAR.writeSlice(blockBuilder, result);
    }
    return blockBuilder.build();
  }
}
