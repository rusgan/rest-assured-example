package org.sora.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jp.co.soramitsu.iroha.java.Transaction;
import jp.co.soramitsu.iroha.java.Utils;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sora.dataproviders.User;
import org.sora.rest.Code;
import org.sora.rest.GeneralResponseDTO;
import org.sora.rest.wallet.TransactionStatus;
import org.sora.rest.wallet.WalletCode;
import org.sora.rest.wallet.WalletMessage;
import org.sora.rest.SoraFilter;
import org.sora.steps.WalletSteps;
import org.sora.utils.PropertiesUtil;
import org.sora.properties.Properties;

import javax.xml.bind.DatatypeConverter;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.sora.utils.AssertUtil.assertPositiveStatus;
import static org.sora.utils.UserUtil.retrieveUser;

@Epic("")
@Feature("")
@Story("")
class Wallet extends BaseTest {

    private WalletSteps walletSteps = new WalletSteps();
    private static User autotestUser;
    private static User receiverUser;
    private String amount = "1";

    @Tags({@Tag("transfer"), @Tag("positive")})
    @ParameterizedTest(name = "{displayName} {arguments}")
    @ValueSource(strings = {"", ""})
    @DisplayName("")
    void positiveSend(String amount){

        //Build and sign transaction
        val tx = Transaction.builder(autotestUser.getAccountId())
                .transferAsset(autotestUser.getAccountId(), receiverUser.getAccountId(), Properties.XOR, Properties.DEFAULT_DESCRIPTION, amount)
                .setQuorum(2)
                .sign(autotestUser.getKeyPair())
                .build();
        val txHash = DatatypeConverter.printHexBinary(Utils.hash(tx)).toLowerCase();

        //Send transaction
        val response = walletSteps.sendTransaction(tx);

        //Assert general response
        assertPositiveStatus(response.getStatus());

        //Get transaction from transaction list
        val transaction = walletSteps.getUserTransactions().getTransactionByHash(txHash);

        //Assert transaction
        assertThat(transaction).isNotNull();
        assertThat(transaction.getAmount()).isEqualTo(amount);
        assertThat(transaction.getPeerId()).isEqualTo(receiverUser.getAccountId());


        //Init filter for receiverUser user
        SoraFilter.initFilter(receiverUser.getKeyPair());

        //Get incoming transaction
        val receiverTransaction = walletSteps.getUserTransactions().getTransactionByHash(txHash);

        //Assert transaction
        assertThat(receiverTransaction).as("Transaction not founded").isNotNull();
        assertThat(receiverTransaction.getAmount()).as("Sent amount differ from amount in Transaction history").isEqualTo(amount);
        assertThat(receiverTransaction.getPeerId()).isEqualTo(autotestUser.getAccountId());
        assertThat(receiverTransaction.isIncoming()).as("Transaction not incoming").isEqualTo(true);

        //Send reverse transaction
        walletSteps.sendTransaction(Transaction.builder(receiverUser.getAccountId())
                .transferAsset(receiverUser.getAccountId(), autotestUser.getAccountId(), Properties.XOR, "Reverse transaction", amount)
                .setQuorum(2)
                .sign(receiverUser.getKeyPair())
                .build());

        //Assert first transaction status
        await().atMost(30, SECONDS).untilAsserted(() -> assertThat(walletSteps.getUserTransactions().getTransactionByHash(txHash).getStatus()).as("Transaction not committed in 30 seconds").isEqualTo(TransactionStatus.COMMITED));
    }


    @Test()
    void negativeSelfSend(){

        //Build and sign transaction
        val tx = Transaction.builder(autotestUser.getAccountId())
                .transferAsset(autotestUser.getAccountId(), autotestUser.getAccountId(), Properties.XOR, Properties.DEFAULT_DESCRIPTION, amount)
                .setQuorum(2)
                .sign(autotestUser.getKeyPair())
                .build();

        //Assert response
        val response = walletSteps.sendTransaction(tx);
        assertThat(response.getStatus().getMessage()).isEqualTo(WalletMessage.SAME_ACCOUNT_MESSAGE + " " + autotestUser.getAccountId());
    }

    @Test()
    void negativeAnotherDomain(){

        //Build and sign transaction
        val tx = Transaction.builder(autotestUser.getAccountId())
                .transferAsset(autotestUser.getAccountId(), "test@notary", Properties.XOR, Properties.DEFAULT_DESCRIPTION, amount)
                .setQuorum(2)
                .sign(autotestUser.getKeyPair())
                .build();

        //Assert response
        val response = walletSteps.sendTransaction(tx);
        assertThat(response.getStatus().getCode()).isEqualTo(Code.USER_NOT_REGISTERED);
        assertThat(response.getStatus().getMessage()).isEqualTo(WalletMessage.NOT_REGISTERED_MESSAGE, "test@notary");
    }

    @Test()
    void negativeWrongAccountId(){

        //Build and sign transaction
        val tx = Transaction.builder(autotestUser.getAccountId())
                .transferAsset(autotestUser.getAccountId(), "random@sora", Properties.XOR, Properties.DEFAULT_DESCRIPTION, amount)
                .setQuorum(2)
                .sign(autotestUser.getKeyPair())
                .build();

        //Assert response
        val response = walletSteps.sendTransaction(tx);
        assertThat(response.getStatus().getCode()).isEqualTo(Code.USER_NOT_REGISTERED);
        assertThat(response.getStatus().getMessage()).isEqualTo(WalletMessage.NOT_REGISTERED_MESSAGE, "random@sora");
    }

    @Test
    void negativeInvalidTransaction(){

        //Build and sign invalid transaction
        val tx = Transaction.builder(autotestUser.getAccountId()).createDomain("some", "some").setQuorum(2).sign(autotestUser.getKeyPair()).build();

        //Assert response
        GeneralResponseDTO response = walletSteps.sendTransaction(tx);
        assertThat(response.getStatus().getCode()).isEqualTo(WalletCode.INVALID_TRANSACTION_TYPE);
        assertThat(response.getStatus().getMessage()).startsWith(WalletMessage.UNSUPPORTED);
    }

    @BeforeAll
    static void setUp(){

        //Init properties
        PropertiesUtil.initProperties();

        //Init user from file
        autotestUser = retrieveUser("");
        receiverUser = retrieveUser("");
    }

    @BeforeEach
    void setUpFilter(){

        //Init filter for autotest user
        SoraFilter.initFilter(autotestUser.getKeyPair());
    }
}
