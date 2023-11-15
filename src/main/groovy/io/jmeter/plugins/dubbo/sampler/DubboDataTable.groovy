package io.jmeter.plugins.dubbo.sampler

class DubboDataTable implements Serializable {
    private String value
    private String type

    String getValue() {
        return value
    }

    void setValue(String value) {
        this.value = value
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }
}
