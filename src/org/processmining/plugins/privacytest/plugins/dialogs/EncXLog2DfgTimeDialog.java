package org.processmining.plugins.privacytest.plugins.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.Classifiers;
import org.processmining.plugins.InductiveMiner.Classifiers.ClassifierWrapper;
import org.processmining.plugins.InductiveMiner.dfgOnly.log2logInfo.IMLog2IMLogInfo;

import com.fluxicon.slickerbox.factory.SlickerFactory;

import de.henku.jpaillier.PublicKey;

public class EncXLog2DfgTimeDialog extends JPanel{
	private static final long serialVersionUID = -3128018697672831767L;
	private final JComboBox<Variant> variantCombobox;
	private final JComboBox<ClassifierWrapper> classifierCombobox;
	
	public abstract class Variant {
		@Override
		public abstract String toString();

		public abstract boolean hasNoise();

		public abstract IMLog2IMLogInfo getIMLog2IMLogInfo();
	}

	public class VariantEncrypted extends Variant {
		PublicKey publicKey;
		
		public VariantEncrypted(PublicKey publicKey) {
			this.publicKey = publicKey;
		}
		
		public String toString() {
			return "use event classes - (IMd, IMfd, IMcd) with encrypted timestamps";
		}

		public boolean hasNoise() {
			return false;
		}

		public IMLog2IMLogInfo getIMLog2IMLogInfo() {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public EncXLog2DfgTimeDialog(XLog log, PublicKey publicKey) {
		SlickerFactory factory = SlickerFactory.instance();

		int gridy = 1;

		setLayout(new GridBagLayout());

		//variant
		{
			final JLabel variantLabel = factory.createLabel("Variant");
			{
				GridBagConstraints cVariantLabel = new GridBagConstraints();
				cVariantLabel.gridx = 0;
				cVariantLabel.gridy = gridy;
				cVariantLabel.weightx = 0.4;
				cVariantLabel.anchor = GridBagConstraints.NORTHWEST;
				add(variantLabel, cVariantLabel);
			}
			
			variantCombobox = factory.createComboBox(
					new Variant[] { new VariantEncrypted(publicKey)});
			{
				GridBagConstraints cVariantCombobox = new GridBagConstraints();
				cVariantCombobox.gridx = 1;
				cVariantCombobox.gridy = gridy;
				cVariantCombobox.anchor = GridBagConstraints.NORTHWEST;
				cVariantCombobox.fill = GridBagConstraints.HORIZONTAL;
				cVariantCombobox.weightx = 0.6;
				add(variantCombobox, cVariantCombobox);
				variantCombobox.setSelectedIndex(0);
			}
		}

		gridy++;

		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}

		gridy++;

		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}

		gridy++;

		//classifiers
		{
			final JLabel classifierLabel = factory.createLabel("Event classifier");
			GridBagConstraints cClassifierLabel = new GridBagConstraints();
			cClassifierLabel.gridx = 0;
			cClassifierLabel.gridy = gridy;
			cClassifierLabel.weightx = 0.4;
			cClassifierLabel.anchor = GridBagConstraints.NORTHWEST;
			add(classifierLabel, cClassifierLabel);
		}
		
		classifierCombobox = factory.createComboBox(Classifiers.getClassifiers(log));
		{
			GridBagConstraints cClassifiers = new GridBagConstraints();
			cClassifiers.gridx = 1;
			cClassifiers.gridy = gridy;
			cClassifiers.anchor = GridBagConstraints.NORTHWEST;
			cClassifiers.fill = GridBagConstraints.HORIZONTAL;
			cClassifiers.weightx = 0.6;
			add(classifierCombobox, cClassifiers);
		}

		gridy++;

		{
			GridBagConstraints gbcFiller = new GridBagConstraints();
			gbcFiller.weighty = 1.0;
			gbcFiller.gridy = gridy;
			gbcFiller.fill = GridBagConstraints.BOTH;
			add(Box.createGlue(), gbcFiller);
		}
	}

	public IMLog2IMLogInfo getIMLog2IMLogInfo() {
		return ((Variant) variantCombobox.getSelectedItem()).getIMLog2IMLogInfo();
	}
	
	public XEventClassifier getClassifier() {
		return ((ClassifierWrapper) classifierCombobox.getSelectedItem()).classifier;
	}
}
