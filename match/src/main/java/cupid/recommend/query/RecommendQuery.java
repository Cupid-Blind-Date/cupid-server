package cupid.recommend.query;

import cupid.recommend.query.param.RecommendQueryParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendQuery {

    List<Long> findMaleRecommended(RecommendQueryParam param);

    List<Long> findFemaleRecommended(RecommendQueryParam param);

    List<Long> findBothGenderRecommended(RecommendQueryParam param);
}
