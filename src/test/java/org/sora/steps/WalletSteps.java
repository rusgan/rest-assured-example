package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import iroha.protocol.TransactionOuterClass;
import lombok.val;
import org.sora.dataproviders.User;
import org.sora.rest.GeneralResponseDTO;
import org.sora.rest.SoraFilter;
import org.sora.rest.wallet.WalletEndpoints;
import org.sora.rest.wallet.dto.*;
import org.sora.properties.Properties;


import static io.restassured.RestAssured.given;
import static jp.co.soramitsu.iroha.java.Query.builder;
import static org.sora.rest.Specifications.initSpec;

public class WalletSteps extends BaseSteps {


    private RequestSpecification walletSpec = initSpec(Properties.WALLET_URL);

    @Step("Get account balance")
    public double getBalance(User user){

        val request = new BalanceDTO()
                .createListAddAsset(Properties.XOR)
                .setQuery(builder(user.getAccountId(), 1).getAccountAssets(user.getAccountId()).buildSigned(user.getKeyPair()).toByteArray());

        val assets =  given()
                .filter(SoraFilter.filter)
                .spec(walletSpec)
                .body(request)
                .when()
                .post(WalletEndpoints.BALANCE)
                .then()
                .statusCode(200).extract().as(AccountAssetsDTO.class);

        return assets.getAsset(Properties.XOR).getBalance();
    }

    @Step("Send transaction")
    public GeneralResponseDTO sendTransaction(TransactionOuterClass.Transaction tx){

        SendTransactionDTO tr = new SendTransactionDTO();
        tr.setTransaction(tx.toByteArray());

        return given()
                .filter(SoraFilter.filter)
                .spec(walletSpec)
                .body(new SendTransactionDTO()
                .setTransaction(tx.toByteArray()))
                .when()
                .post(WalletEndpoints.TRANSACTION)
                .then()
                .statusCode(200).extract().as(GeneralResponseDTO.class);
    }

    @Step("Get user transactions")
    public AccountTransactionsDTO getUserTransactions(){

        return given()
                .filter(SoraFilter.filter)
                .spec(walletSpec)
                .queryParam("count", 2)
                .queryParam("offset", 0)
                .when()
                .get(WalletEndpoints.TRANSACTION)
                .then()
                .statusCode(200).extract().as(AccountTransactionsDTO.class);
    }
}
