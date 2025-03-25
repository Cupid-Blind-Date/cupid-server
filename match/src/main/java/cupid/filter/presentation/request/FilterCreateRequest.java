package cupid.filter.presentation.request;

import cupid.filter.application.command.FilterCreateCommand;
import cupid.filter.domain.GenderCondition;

public record FilterCreateRequest(
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int maxIncludeDistanceFromMe,
        boolean permitExcessDistance,
        GenderCondition genderCondition
) {
    public FilterCreateCommand toCommand(Long memberId) {
        return new FilterCreateCommand(
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
