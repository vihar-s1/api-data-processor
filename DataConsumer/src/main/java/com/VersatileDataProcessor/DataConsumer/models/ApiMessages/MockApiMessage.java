package com.VersatileDataProcessor.DataConsumer.models.ApiMessages;
import com.VersatileDataProcessor.DataConsumer.models.DataSource;
import com.VersatileDataProcessor.DataConsumer.models.StandardApiMessage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(as = MockApiMessage.class)
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
