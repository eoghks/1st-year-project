package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNamespace is a Querydsl query type for Namespace
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNamespace extends EntityPathBase<Namespace> {

    private static final long serialVersionUID = 1257270512L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNamespace namespace = new QNamespace("namespace");

    public final QAccessKey accessKey;

    public final QNamespacePK pk;

    public QNamespace(String variable) {
        this(Namespace.class, forVariable(variable), INITS);
    }

    public QNamespace(Path<? extends Namespace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNamespace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNamespace(PathMetadata metadata, PathInits inits) {
        this(Namespace.class, metadata, inits);
    }

    public QNamespace(Class<? extends Namespace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accessKey = inits.isInitialized("accessKey") ? new QAccessKey(forProperty("accessKey")) : null;
        this.pk = inits.isInitialized("pk") ? new QNamespacePK(forProperty("pk")) : null;
    }

}

