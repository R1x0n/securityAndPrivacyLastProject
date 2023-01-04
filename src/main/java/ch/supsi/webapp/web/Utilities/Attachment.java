package ch.supsi.webapp.web.Utilities;

import lombok.*;
import org.apache.commons.io.FileUtils;

import javax.persistence.Embeddable;
import javax.persistence.Lob;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Attachment {
    @Lob
    private byte[] bytes;

    private String name;

    private String contentType;

    private Long size;

    public String getReadeableSize() {
        return FileUtils.byteCountToDisplaySize(size);
    }

}