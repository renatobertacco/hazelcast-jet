package com.hazelcast.jet.impl.job.deployment.processors;

import com.hazelcast.jet.container.ProcessorContext;
import com.hazelcast.jet.data.io.ConsumerOutputStream;
import com.hazelcast.jet.data.io.ProducerInputStream;
import com.hazelcast.jet.processor.Processor;

import static org.junit.Assert.fail;

public class PrintPersonVertex implements Processor {

    public PrintPersonVertex() {
    }

    @Override
    public boolean process(ProducerInputStream inputStream,
                           ConsumerOutputStream outputStream,
                           String sourceName, ProcessorContext processorContext) throws Exception {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        contextClassLoader.loadClass("com.sample.pojo.person.Person$Appereance");
        try {
            contextClassLoader.loadClass("com.sample.pojo.car.Car");
            fail();
        } catch (ClassNotFoundException ignored) {
        }
        outputStream.consumeStream(inputStream);
        return true;
    }
}
