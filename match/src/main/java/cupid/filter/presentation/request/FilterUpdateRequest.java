package cupid.filter.presentation.request;

import cupid.filter.application.command.FilterUpdateCommand;
import cupid.filter.domain.GenderCondition;

public record FilterUpdateRequest(
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int maxIncludeDistanceFromMe,
        boolean permitExcessDistance,
        GenderCondition genderCondition
) {
    public FilterUpdateCommand toCommand(Long memberId) {
        return new FilterUpdateCommand(
                memberId,
                maxIncludeAge,
                minIncludeAge,
                permitExcessAge,
                maxIncludeDistanceFromMe,
                permitExcessDistance,
                genderCondition
        );
    }
}
