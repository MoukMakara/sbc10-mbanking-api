package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.account.dto.*;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.mapper.AccountMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;

    private final AccountMapper accountMapper;


    @Override
    public void updateTransferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest) {

        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setTransferLimit(accountTransferLimitRequest.amount());
        accountRepository.save(account);
    }

    @Override
    public void hideAccount(String actNo) {

        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setIsHidden(true);
        accountRepository.save(account);
    }

    @Override
    public AccountResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest) {

        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setAlias(accountRenameRequest.alias());
        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    // updateAccount
    @Override
    public void updateAccount(String actNo, AccountUpdateRequest accountUpdateRequest) {
        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        // Update account information
        account.setAlias(accountUpdateRequest.alias());
        account.setActName(accountUpdateRequest.actName());
        account.setBalance(accountUpdateRequest.balance());
        account.setTransferLimit(accountUpdateRequest.transferLimit());
        account.setIsHidden(accountUpdateRequest.isHidden());

        accountRepository.save(account);
    }

    @Override
    public void softDeleteAccount(String actNo, AccountSoftDeleteRequest accountSoftDeleteRequest) {
        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setIsDeleted(accountSoftDeleteRequest.isDeleted());
//      account.setIsDeleted(true);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String actNo) {
        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        accountRepository.delete(account);
//        accountRepository.save(account);
    }

    @Override
    public AccountResponse createNew(AccountCreateRequest accountCreateRequest) {

        // Validate account type
        AccountType accountType = accountTypeRepository
                .findByAlias(accountCreateRequest.accountTypeAlias())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account Type has not been found"
                ));

        // Validate user
        User user = userRepository
                .findByUuid(accountCreateRequest.userUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found"
                ));

        // Validate account no
        if (accountRepository.existsByActNo(accountCreateRequest.actNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account no has already been existed");
        }

        // Validate balance
        if (accountCreateRequest.balance().compareTo(BigDecimal.valueOf(10)) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Balance 10$ is required to create account"
            );
        }

        // Transfer DTO to Domain Model
        Account account = accountMapper.fromAccountCreateRequest(accountCreateRequest);
        account.setAccountType(accountType);
        account.setUser(user);

        // System generate data
        account.setActName(user.getName());
        account.setIsHidden(false);
        account.setTransferLimit(BigDecimal.valueOf(1000));

        // Save account into database and get latest data back
        account = accountRepository.save(account);

        // Transfer Domain Model to DTO
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Page<AccountResponse> findList(int pageNumber, int pageSize) {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Account> accounts = accountRepository.findAll(pageRequest);

        return accounts.map(accountMapper::toAccountResponse);
    }

    @Override
    public AccountResponse findByActNo(String actNo) {

        // Validate account no
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        return accountMapper.toAccountResponse(account);
    }

}
