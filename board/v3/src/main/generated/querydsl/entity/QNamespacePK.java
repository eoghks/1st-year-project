package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNamespacePK is a Querydsl query type for NamespacePK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QNamespacePK extends BeanPath<NamespacePK> {

    private static final long serialVersionUID = 1351154411L;

    public static final QNamespacePK namespacePK = new QNamespacePK("namespacePK");

    public final StringPath host = createString("host");

    public final StringPath namespace = createString("namespace");

    public QNamespacePK(String variable) {
        super(NamespacePK.class, forVariable(variable));
    }

    public QNamespacePK(Path<? extends NamespacePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNamespacePK(PathMetadata metadata) {
        super(NamespacePK.class, metadata);
    }

}

