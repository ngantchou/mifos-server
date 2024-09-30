
package org.apache.fineract.organisation.teller.handler;

import lombok.RequiredArgsConstructor;
import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.organisation.teller.service.TellerWritePlatformService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CommandType(entity = "CASHIER", action = "OPENCASHIERSESSION")
public class OpenSessionCashierToTellerCommandHandler implements NewCommandSourceHandler {

    private final TellerWritePlatformService writePlatformService;

    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.openCashierSession(command.entityId(), command.subentityId(), command);
    }
}
