package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Map Account to AccountResponse
    // Source = Account
    // Target = AccountResponse
    // @Mapping(source = "accountType.alias", target = "accountTypeAlias")
    AccountResponse toAccountResponse(Account account);

    Account fromAccountCreateRequest(AccountCreateRequest accountCreateRequest);

}
