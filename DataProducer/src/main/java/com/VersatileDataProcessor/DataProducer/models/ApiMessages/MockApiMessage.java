package com.VersatileDataProcessor.DataProducer.models.ApiMessages;
import com.VersatileDataProcessor.DataProducer.models.DataSource;
import com.VersatileDataProcessor.DataProducer.models.StandardApiMessage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MockApiMessage implements ApiMessageInterface {
    private String id;
    private String mockData;
    private  final DataSource dataSource = DataSource.MOCK;

    @Override
    public StandardApiMessage toStandardApiMessage() {
        StandardApiMessage message = new StandardApiMessage();

        message.setData(this);
        message.setId(this.dataSource.name() + "-" + this.id);

        return  message;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
