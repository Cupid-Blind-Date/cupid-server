package cupid.recommend.query;

import cupid.recommend.query.param.RecommendByIdsQueryParam;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.recommend.query.param.RecommendWithoutDistanceQueryParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendQuery {

    List<Long> findRecommended(RecommendQueryParam param);

    List<Long> findRecommendedWithoutDistance(RecommendWithoutDistanceQueryParam param);

    List<Long> findRecommendedByIdsIn(RecommendByIdsQueryParam param);
}
