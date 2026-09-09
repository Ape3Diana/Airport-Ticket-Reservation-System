package com.aeroport.infrastructure;

import com.aeroport.domain.IExportStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class XmlExportStrategy implements IExportStrategy {

    // XmlMapper este echivalentul lui ObjectMapper, dar specializat pentru XML.
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public byte[] exportData(List<Map<String, Object>> data) throws Exception {
        // withRootName("DateExportate") forțează Jackson să împacheteze toate elementele listei
        // în interiorul unui tag principal de tipul <DateExportate>...</DateExportate>
        return xmlMapper.writer().withRootName("DateExportate").writeValueAsBytes(data);
    }

    @Override
    public String getContentType() { return "application/xml"; }

    @Override
    public String getFileExtension() { return "xml"; }

    @Override
    public String getFormatName() { return "xml"; }
}