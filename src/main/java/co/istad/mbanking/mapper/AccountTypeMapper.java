package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.features.accounttype.dto.AccountTypeRequest;
import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;
import co.istad.mbanking.features.accounttype.dto.AccountTypeUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    AccountTypeResponse toAccountTypeResponse(AccountType accountType);

    // Partially map
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromAccountTypeUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest,
                                      @MappingTarget AccountType accountType);

    AccountType fromAccountTypeUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest);

    AccountType fromAccountTypeRequest(AccountTypeRequest accountTypeRequest);

    List<AccountTypeResponse> toAccountTypeResponseList(List<AccountType> accountTypes);

}
