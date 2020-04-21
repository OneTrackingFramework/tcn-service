/**
 *
 */
package one.tracking.framework.tcn.web;

import one.tracking.framework.tcn.dto.KeyDto;
import one.tracking.framework.tcn.dto.PayloadDto;
import one.tracking.framework.tcn.service.TcnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

@RestController
@RequestMapping(path = "native")
public class TcnNativeController {

    @Autowired
    private TcnService service;

    @RequestMapping(method = RequestMethod.POST)
    public void handlePush(
            @RequestBody final byte[] payload) {

        if (payload.length < 68) {
            return;
        }
        String rvk = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 0, 32));
        String tck = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 32, 64));
        int j1 = bytesToInt(Arrays.copyOfRange(payload, 64, 66));
        int j2 = bytesToInt(Arrays.copyOfRange(payload, 66, 68));

        String memo = null;
        if (payload.length > 69) {
            int range = bytesToInt(Arrays.copyOfRange(payload, 69, 70));

            if (range + 70 == payload.length) {
                memo = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 70, range));
            }

        }

        PayloadDto payloadDto = PayloadDto.builder()
                .rvk(rvk)
                .tck(tck)
                .j1(j1)
                .j2(j2)
                .memo(memo)
                .build();

        this.service.handlePush(payloadDto);
    }


    private int bytesToInt(byte[] bytes) {
        short shortVal = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return shortVal >= 0 ? shortVal : 0x10000 + shortVal;

    }

}
