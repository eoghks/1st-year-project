package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccessKey is a Querydsl query type for AccessKey
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccessKey extends EntityPathBase<AccessKey> {

    private static final long serialVersionUID = -2110005776L;

    public static final QAccessKey accessKey = new QAccessKey("accessKey");

    public final StringPath accessKeyId = createString("accessKeyId");

    public final StringPath hostName = createString("hostName");

    public final ListPath<Namespace, QNamespace> namespaces = this.<Namespace, QNamespace>createList("namespaces", Namespace.class, QNamespace.class, PathInits.DIRECT2);

    public final StringPath region = createString("region");

    public final NumberPath<Integer> scanInterval = createNumber("scanInterval", Integer.class);

    public final StringPath scanKey = createString("scanKey");

    public final NumberPath<Integer> scanType = createNumber("scanType", Integer.class);

    public QAccessKey(String variable) {
        super(AccessKey.class, forVariable(variable));
    }

    public QAccessKey(Path<? extends AccessKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccessKey(PathMetadata metadata) {
        super(AccessKey.class, metadata);
    }

}

