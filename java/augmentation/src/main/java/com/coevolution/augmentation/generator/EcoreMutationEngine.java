
package com.coevolution.augmentation.generator;

import com.coevolution.augmentation.mutation.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import java.io.File;
import java.util.*;

public class EcoreMutationEngine {

    private final List<MutationOperator> operators;

    public EcoreMutationEngine() {
        operators = new ArrayList<>();
        operators.add(new AddEClassMutation());
        operators.add(new RemoveEClassMutation());
        operators.add(new AddAttributeMutation());
        operators.add(new RemoveAttributeMutation());
        operators.add(new ChangeTypeMutation());
        operators.add(new AddReferenceMutation());
        operators.add(new RemoveReferenceMutation());
        operators.add(new ChangeMultiplicityMutation());
        operators.add(new ChangeAbstractMutation());
        operators.add(new AddSuperTypeMutation());
    }

    public EPackage loadEcore(File f) throws Exception {
        ResourceSet rs = new ResourceSetImpl();
        rs.getResourceFactoryRegistry()
          .getExtensionToFactoryMap()
          .put("ecore", new XMIResourceFactoryImpl());
        Resource res = rs.getResource(URI.createFileURI(f.getAbsolutePath()), true);
        res.load(null);
        return (EPackage) res.getContents().get(0);
    }

    public EPackage clonePackage(EPackage p) { return EcoreUtil.copy(p); }

    public void saveEcore(EPackage pkg, File out) throws Exception {
        ResourceSet rs = new ResourceSetImpl();
        rs.getResourceFactoryRegistry()
          .getExtensionToFactoryMap()
          .put("ecore", new XMIResourceFactoryImpl());
        out.getParentFile().mkdirs();
        Resource res = rs.createResource(URI.createFileURI(out.getAbsolutePath()));
        res.getContents().add(pkg);
        res.save(Collections.singletonMap("SERIALIZE_TRANSIENT_VALUES", Boolean.FALSE));
    }

    public List<MutationCandidate> generateMutations(EPackage original) {
        List<MutationCandidate> list = new ArrayList<>();
        for (MutationOperator op : operators) {
            if (op.canApply(original)) {
                EPackage clone = clonePackage(original);
                MutationResult r = op.apply(clone);
                if (r.isSuccess()) list.add(new MutationCandidate(op, clone, r));
            }
        }
        return list;
    }

    public static class MutationCandidate {
        public final MutationOperator operator;
        public final EPackage         mutatedPackage;
        public final MutationResult   result;
        public MutationCandidate(MutationOperator op, EPackage pkg, MutationResult r) {
            this.operator = op; this.mutatedPackage = pkg; this.result = r;
        }
    }
}