package com.example.assistant;

import java.util.ArrayList;

public class AssistantRespond {
    public String object;
    public ArrayList<Data> data;
    public String first_id;
    public String last_id;
    public boolean has_more;
    public void setObject(String object) {
        this.object = object;
    }
    public String getObject() {
        return object;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
    public ArrayList<Data> getData() {
        return data;
    }

    public void setFirst_id(String first_id) {
        this.first_id = first_id;
    }
    public String getFirst_id() {
        return first_id;
    }

    public void setLast_id(String last_id) {
        this.last_id = last_id;
    }
    public String getLast_id() {
        return last_id;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }
    public boolean getHas_more() {
        return has_more;
    }
    public class Data {

        public String id;
        public String object;
        public long created_at;
        public String assistant_id;
        public String thread_id;
        public String run_id;
        public String role;
        public ArrayList<Content> content;
        public ArrayList<File_ids> file_ids;
        public Metadata metadata;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setObject(String object) {
            this.object = object;
        }
        public String getObject() {
            return object;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }
        public long getCreated_at() {
            return created_at;
        }

        public void setAssistant_id(String assistant_id) {
            this.assistant_id = assistant_id;
        }
        public String getAssistant_id() {
            return assistant_id;
        }

        public void setThread_id(String thread_id) {
            this.thread_id = thread_id;
        }
        public String getThread_id() {
            return thread_id;
        }

        public void setRun_id(String run_id) {
            this.run_id = run_id;
        }
        public String getRun_id() {
            return run_id;
        }

        public void setRole(String role) {
            this.role = role;
        }
        public String getRole() {
            return role;
        }

        public void setContent(ArrayList<Content> content) {
            this.content = content;
        }
        public ArrayList<Content> getContent() {
            return content;
        }

        public void setFile_ids(ArrayList<File_ids> file_ids) {
            this.file_ids = file_ids;
        }
        public ArrayList<File_ids> getFile_ids() {
            return file_ids;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }
        public Metadata getMetadata() {
            return metadata;
        }

        public class Content {

            public String type;
            public Text text;
            public void setType(String type) {
                this.type = type;
            }
            public String getType() {
                return type;
            }

            public void setText(Text text) {
                this.text = text;
            }
            public Text getText() {
                return text;
            }

            public class Text {

                public String value;
                public ArrayList<Annotations> annotations;
                public void setValue(String value) {
                    this.value = value;
                }
                public String getValue() {
                    return value;
                }

                public void setAnnotations(ArrayList<Annotations> annotations) {
                    this.annotations = annotations;
                }
                public ArrayList<Annotations> getAnnotations() {
                    return annotations;
                }

                public class Annotations {

                }

            }

        }

        public class File_ids {

        }

        public class Metadata {

        }

    }
}
