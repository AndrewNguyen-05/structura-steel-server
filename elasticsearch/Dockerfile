ARG ES_VERSION=8.11.4
FROM docker.elastic.co/elasticsearch/elasticsearch:${ES_VERSION}

# Cài đặt plugin analysis-icu để hỗ trợ tiếng Việt tốt hơn
RUN bin/elasticsearch-plugin install analysis-icu